package com.smart1.appsmartweb.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.smart1.appsmartweb.config.BancadaProperties;
import com.smart1.appsmartweb.model.enums.StationType;
import com.smart1.appsmartweb.service.connection.PlcConnectionManager;
import com.smart1.appsmartweb.service.connection.PlcReaderTask;
import com.smart1.appsmartweb.service.connection.SmartService;
import com.smart1.appsmartweb.util.PlcConnector;

import jakarta.annotation.PreDestroy;

@Controller
public class ConnectionController {

    private static final Logger log = LoggerFactory.getLogger(ConnectionController.class);

    private final SmartService smartService;
    private final BancadaProperties bancadaProperties;

    private final Map<String, String> leiturasCache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService leituraExecutor = Executors.newScheduledThreadPool(4);
    private final Map<String, ScheduledFuture<?>> leituraFutures = new ConcurrentHashMap<>();

    public ConnectionController(SmartService smartService, BancadaProperties bancadaProperties) {
        this.smartService = smartService;
        this.bancadaProperties = bancadaProperties;
    }

    private static volatile byte[] dadosClp1;
    private static volatile byte[] dadosClp2;
    private static volatile byte[] dadosClp3;
    private static volatile byte[] dadosClp4;

    @PostMapping("/start-leituras")
    public ResponseEntity<String> startLeituras(@RequestBody Map<String, String> ips) {
        // Registra dinamicamente os IPs informados pelo usuário para cada estação (corrige BUG-14)
        PlcConnectionManager.registerStationIps(ips);
        long pollIntervalMs = bancadaProperties.getPlc().getPollIntervalMs();

        ips.forEach((nome, ip) -> {
            String key = nome.toLowerCase().trim();
            if (!leituraFutures.containsKey(key)) {
                PlcConnector plcConnector = PlcConnectionManager.getConexao(ip);
                if (plcConnector == null) {
                    log.error("Erro ao obter conexão com o CLP '{}' ({})", key, ip);
                    return;
                }

                StationType station = StationType.fromKey(key);
                if (station == null) {
                    log.warn("Nome de CLP inválido recebido em /start-leituras: {}", nome);
                    return;
                }

                PlcReaderTask task = switch (station) {
                    case ESTOQUE -> new PlcReaderTask(
                            plcConnector, key, station.getDbNumber(), station.getOffset(), station.getReadSize(),
                            dados -> {
                                ConnectionController.dadosClp1 = dados;
                                try {
                                    smartService.clpEstoque(ip, dados);
                                } catch (Exception e) {
                                    log.error("Erro ao processar leitura do CLP Estoque ({}): {}", ip, e.getMessage(), e);
                                }
                                atualizarCache("estoque", dados);
                            });
                    case PROCESSO -> new PlcReaderTask(
                            plcConnector, key, station.getDbNumber(), station.getOffset(), station.getReadSize(),
                            dados -> {
                                ConnectionController.dadosClp2 = dados;
                                smartService.clpProcesso(ip, dados);
                                atualizarCache("processo", dados);
                            });
                    case MONTAGEM -> new PlcReaderTask(
                            plcConnector, key, station.getDbNumber(), station.getOffset(), station.getReadSize(),
                            dados -> {
                                ConnectionController.dadosClp3 = dados;
                                smartService.clpMontagem(ip, dados);
                                atualizarCache("montagem", dados);
                            });
                    case EXPEDICAO -> new PlcReaderTask(
                            plcConnector, key, station.getDbNumber(), station.getOffset(), station.getReadSize(),
                            dados -> {
                                ConnectionController.dadosClp4 = dados;
                                try {
                                    smartService.clpExpedicao(ip, dados);
                                } catch (Exception e) {
                                    log.error("Erro ao processar leitura do CLP Expedição ({}): {}", ip, e.getMessage(), e);
                                }
                                atualizarCache("expedicao", dados);
                            });
                };

                ScheduledFuture<?> future = leituraExecutor.scheduleAtFixedRate(
                        task, 0, pollIntervalMs, TimeUnit.MILLISECONDS);
                leituraFutures.put(key, future);
            }
        });

        return ResponseEntity.ok("Leituras com PlcReaderTask iniciadas.");
    }

    private void atualizarCache(String nome, byte[] dados) {
        StringBuilder sb = new StringBuilder();
        for (byte b : dados) {
            sb.append(String.format("%02X ", b));
        }
        leiturasCache.put(nome, sb.toString().trim());
    }

    @PostMapping("/stop-leituras")
    public ResponseEntity<String> stopLeituras() {
        leituraFutures.forEach((nome, future) -> {
            future.cancel(true);
            log.info("Thread de leitura '{}' cancelada.", nome);
        });
        leituraFutures.clear();
        PlcConnectionManager.encerrarTodasAsConexoes();
        return ResponseEntity.ok("Leituras interrompidas.");
    }

    @PreDestroy
    public void onShutdown() {
        stopLeituras();
        leituraExecutor.shutdownNow();
    }

    @GetMapping("/status-leituras")
    public ResponseEntity<Map<String, Object>> getStatusLeituras() {
        Map<String, Object> status = new HashMap<>();

        boolean ativo = !leituraFutures.isEmpty();
        status.put("ativo", ativo);
        status.put("estoque", leituraFutures.containsKey("estoque"));
        status.put("processo", leituraFutures.containsKey("processo"));
        status.put("montagem", leituraFutures.containsKey("montagem"));
        status.put("expedicao", leituraFutures.containsKey("expedicao"));

        return ResponseEntity.ok(status);
    }

    @PostMapping("/smart/ping")
    public ResponseEntity<Map<String, Boolean>> pingPlcs(@RequestBody Map<String, String> ips) {
        PlcConnectionManager.registerStationIps(ips);
        Map<String, Boolean> fullStatus = smartService.pingPlcs(ips);

        Set<String> modulosValidos = Set.of("estoque", "processo", "montagem", "expedicao");

        Map<String, Boolean> statusFiltrado = new HashMap<>();
        for (String nome : modulosValidos) {
            if (fullStatus.containsKey(nome)) {
                statusFiltrado.put(nome, fullStatus.get(nome));
            }
        }

        return ResponseEntity.ok(statusFiltrado);
    }

}
