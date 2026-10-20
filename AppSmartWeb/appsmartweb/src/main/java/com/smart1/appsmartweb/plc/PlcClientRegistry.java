package com.smart1.appsmartweb.plc;

import org.springframework.stereotype.Component;

import com.smart1.appsmartweb.config.BancadaProperties;
import com.smart1.appsmartweb.model.enums.StationType;
import com.smart1.appsmartweb.service.connection.PlcConnectionManager;
import com.smart1.appsmartweb.util.PlcConnector;

import jakarta.annotation.PostConstruct;

import java.util.Map;

/**
 * Gerenciador Spring de conexões e IPs ativos dos 4 CLPs da bancada.
 * Sincroniza as configurações do BancadaProperties com o PlcConnectionManager.
 */
@Component
public class PlcClientRegistry {

    private final BancadaProperties properties;

    public PlcClientRegistry(BancadaProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        PlcConnectionManager.configure(
                properties.getPlc().getDefaultSubnet(),
                properties.getPlc().getPort(),
                properties.getPlc().isSimulationMode());
    }

    public void registerStationIps(Map<String, String> ips) {
        PlcConnectionManager.registerStationIps(ips);
    }

    public String getActiveIp(StationType station) {
        return PlcConnectionManager.getStationIp(station);
    }

    public PlcConnector getConnection(StationType station) {
        return PlcConnectionManager.getConexao(station);
    }

    public PlcConnector getConnection(String ip) {
        return PlcConnectionManager.getConexao(ip);
    }

    public void closeAll() {
        PlcConnectionManager.encerrarTodasAsConexoes();
    }
}
