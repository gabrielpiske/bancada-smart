package com.smart1.appsmartweb.service.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smart1.appsmartweb.model.enums.StationType;
import com.smart1.appsmartweb.util.PlcConnector;

/**
 * Gerenciador thread-safe de conexões TCP S7 e de IPs dinâmicos das 4 estações (corrige BUG-01 e BUG-14).
 */
public class PlcConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(PlcConnectionManager.class);

    private static final Map<String, PlcConnector> conexoes = new ConcurrentHashMap<>();
    private static final Map<StationType, String> stationIps = new EnumMap<>(StationType.class);

    private static volatile String defaultSubnet = "10.74.241";
    private static volatile int defaultPort = 102;
    private static volatile boolean simulationMode = false;

    static {
        resetStationIpsToDefault();
    }

    public static synchronized void configure(String subnet, int port, boolean simulation) {
        if (subnet != null && !subnet.isBlank()) {
            defaultSubnet = subnet.trim();
        }
        if (port > 0) {
            defaultPort = port;
        }
        simulationMode = simulation;
        resetStationIpsToDefault();
        log.info("PlcConnectionManager configurado: subnet={}, port={}, simulationMode={}",
                defaultSubnet, defaultPort, simulationMode);
    }

    public static synchronized void resetStationIpsToDefault() {
        for (StationType station : StationType.values()) {
            stationIps.put(station, station.defaultIpForSubnet(defaultSubnet));
        }
    }

    public static synchronized void registerStationIps(Map<String, String> ips) {
        if (ips == null) {
            return;
        }
        ips.forEach((key, ip) -> {
            StationType station = StationType.fromKey(key);
            if (station != null && ip != null && !ip.isBlank()) {
                stationIps.put(station, ip.trim());
                log.info("IP ativo registrado para estação {}: {}", station.getKey(), ip.trim());
            }
        });
    }

    public static synchronized void registerStationIp(StationType station, String ip) {
        if (station != null && ip != null && !ip.isBlank()) {
            stationIps.put(station, ip.trim());
        }
    }

    public static synchronized String getStationIp(StationType station) {
        return stationIps.getOrDefault(station, station.defaultIpForSubnet(defaultSubnet));
    }

    public static synchronized PlcConnector getConexao(StationType station) {
        return getConexao(getStationIp(station));
    }

    public static synchronized PlcConnector getConexao(String ip) {
        if (ip == null || ip.isBlank()) {
            return null;
        }
        String normalizedIp = ip.trim();
        PlcConnector connector = conexoes.get(normalizedIp);
        if (connector == null || !connector.isConnected()) {
            log.info("Abrindo conexão com o CLP: {}:{} (simulado={})", normalizedIp, defaultPort, simulationMode);
            connector = new PlcConnector(normalizedIp, defaultPort, simulationMode);
            try {
                connector.connect();
                conexoes.put(normalizedIp, connector);
            } catch (Exception e) {
                log.error("Erro ao conectar ao CLP {}: {}", normalizedIp, e.getMessage());
                return null;
            }
        }
        return connector;
    }

    public static synchronized void desconectar(String ip) {
        if (ip == null) {
            return;
        }
        PlcConnector connector = conexoes.remove(ip.trim());
        if (connector != null) {
            try {
                connector.disconnect();
            } catch (Exception e) {
                log.warn("Erro ao desconectar do CLP {}: {}", ip, e.getMessage());
            }
        }
    }

    public static synchronized void encerrarTodasAsConexoes() {
        log.info("Encerrando todas as conexões com os CLPs ({} ativas)", conexoes.size());
        for (Map.Entry<String, PlcConnector> entry : conexoes.entrySet()) {
            String ip = entry.getKey();
            PlcConnector connector = entry.getValue();
            try {
                if (connector != null) {
                    connector.disconnect();
                }
            } catch (Exception e) {
                log.warn("Erro ao encerrar conexão com {}: {}", ip, e.getMessage());
            }
        }
        conexoes.clear();
    }

    public static boolean isSimulationMode() {
        return simulationMode;
    }

    public static void setSimulationMode(boolean simulation) {
        simulationMode = simulation;
    }

    public static boolean pingIp(String ip) {
        if (ip == null || ip.isBlank()) {
            return false;
        }
        if (simulationMode) {
            return true;
        }
        try {
            InetAddress address = InetAddress.getByName(ip.trim());
            return address.isReachable(1000);
        } catch (IOException e) {
            return false;
        }
    }
}
