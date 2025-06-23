package com.smart1.appsmartweb.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.smart1.appsmartweb.service.connection.PlcConnectionManager;
import com.smart1.appsmartweb.service.connection.PlcReaderTask;
import com.smart1.appsmartweb.service.connection.SmartService;
import com.smart1.appsmartweb.util.PlcConnector;

@Controller
public class ConnectionController {

    private final SmartService smartService;

    private final Map<String, String> leiturasCache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService leituraExecutor = Executors.newScheduledThreadPool(4);
    private final Map<String, ScheduledFuture<?>> leituraFutures = new ConcurrentHashMap<>();

    // pra evitar erros fazer injeção de dependencias a moda antiga
    public ConnectionController(SmartService smartService) {
        this.smartService = smartService;
    }

    private static byte[] dadosClp1;
    private static byte[] dadosClp2;
    private static byte[] dadosClp3;
    private static byte[] dadosClp4;

    @PostMapping("/start-leituras")
    public ResponseEntity<String> startLeituras(@RequestBody Map<String, String> ips) {
        ips.forEach((nome, ip) -> {
            if (!leituraFutures.containsKey(nome)) {
                PlcConnector plcConnector = PlcConnectionManager.getConexao(ip);
                if (plcConnector == null) {
                    System.err.println("Erro ao obter conexão com o CLP: " + ip);
                    return; // ignora esse CLP e continua com os demais
                }

                PlcReaderTask task = null;
                switch (nome.toLowerCase()) {
                    case "estoque" ->
                        task = new PlcReaderTask(plcConnector, nome, 9, 0, 111, dados -> {
                            ConnectionController.dadosClp1 = dados;
                            smartService.clpEstoque(ip, dados);
                            atualizarCache("estoque", dados);
                        });

                    case "processo" ->
                        task = new PlcReaderTask(plcConnector, nome, 2, 0, 9, dados -> {
                            ConnectionController.dadosClp2 = dados;
                            smartService.clpProcesso(ip, dados);
                            atualizarCache("processo", dados);
                        });

                    case "montagem" ->
                        task = new PlcReaderTask(plcConnector, nome, 57, 0, 9, dados -> {
                            ConnectionController.dadosClp3 = dados;
                            smartService.clpMontagem(ip, dados);
                            atualizarCache("montagem", dados);
                        });

                    case "expedicao" ->
                        task = new PlcReaderTask(plcConnector, nome, 9, 0, 48, dados -> {
                            ConnectionController.dadosClp4 = dados;
                            smartService.clpExpedicao(ip, dados);
                            atualizarCache("expedicao", dados);
                        });

                    default -> {
                        System.err.println("Nome de CLP inválido: " + nome);
                        return;
                    }
                }

                if (task != null) {
                    ScheduledFuture<?> future = leituraExecutor.scheduleAtFixedRate(task, 0, 800,
                            TimeUnit.MILLISECONDS);
                    leituraFutures.put(nome, future);
                }
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
            System.out.println("Thread de leitura '" + nome + "' cancelada.");
        });
        leituraFutures.clear();
        PlcConnectionManager.encerrarTodasAsConexoes();
        return ResponseEntity.ok("Leituras interrompidas.");
    }

    @PostMapping("/smart/ping")
    public ResponseEntity<Map<String, Boolean>> pingPlcs(@RequestBody Map<String, String> ips) {
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
