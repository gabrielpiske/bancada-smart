package com.smart1.appsmartweb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Propriedades externalizadas da Bancada Smart 4.0 (CLPs Siemens S7 e ESP32).
 */
@Component
@ConfigurationProperties(prefix = "bancada")
public class BancadaProperties {

    private Plc plc = new Plc();
    private Esp32 esp32 = new Esp32();

    public Plc getPlc() {
        return plc;
    }

    public void setPlc(Plc plc) {
        this.plc = plc;
    }

    public Esp32 getEsp32() {
        return esp32;
    }

    public void setEsp32(Esp32 esp32) {
        this.esp32 = esp32;
    }

    public static class Plc {
        private String defaultSubnet = "10.74.241";
        private int port = 102;
        private long pollIntervalMs = 800;
        private int connectTimeoutMs = 2000;
        private int readTimeoutMs = 2000;
        private boolean readOnly = false;
        private boolean simulationMode = false;

        public String getDefaultSubnet() {
            return defaultSubnet;
        }

        public void setDefaultSubnet(String defaultSubnet) {
            this.defaultSubnet = defaultSubnet;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public long getPollIntervalMs() {
            return pollIntervalMs;
        }

        public void setPollIntervalMs(long pollIntervalMs) {
            this.pollIntervalMs = pollIntervalMs;
        }

        public int getConnectTimeoutMs() {
            return connectTimeoutMs;
        }

        public void setConnectTimeoutMs(int connectTimeoutMs) {
            this.connectTimeoutMs = connectTimeoutMs;
        }

        public int getReadTimeoutMs() {
            return readTimeoutMs;
        }

        public void setReadTimeoutMs(int readTimeoutMs) {
            this.readTimeoutMs = readTimeoutMs;
        }

        public boolean isReadOnly() {
            return readOnly;
        }

        public void setReadOnly(boolean readOnly) {
            this.readOnly = readOnly;
        }

        public boolean isSimulationMode() {
            return simulationMode;
        }

        public void setSimulationMode(boolean simulationMode) {
            this.simulationMode = simulationMode;
        }

        public String getDefaultEstoqueIp() {
            return defaultSubnet + ".10";
        }

        public String getDefaultProcessoIp() {
            return defaultSubnet + ".20";
        }

        public String getDefaultMontagemIp() {
            return defaultSubnet + ".30";
        }

        public String getDefaultExpedicaoIp() {
            return defaultSubnet + ".40";
        }
    }

    public static class Esp32 {
        private String url = "http://10.74.241.245:80/api/move_pos";

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
