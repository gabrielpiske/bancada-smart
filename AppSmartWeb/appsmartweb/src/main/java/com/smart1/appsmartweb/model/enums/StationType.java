package com.smart1.appsmartweb.model.enums;

public enum StationType {
    ESTOQUE("estoque", 9, 0, 111, ".10"),
    PROCESSO("processo", 2, 0, 9, ".20"),
    MONTAGEM("montagem", 57, 0, 9, ".30"),
    EXPEDICAO("expedicao", 9, 0, 48, ".40");

    private final String key;
    private final int dbNumber;
    private final int offset;
    private final int readSize;
    private final String hostSuffix;

    StationType(String key, int dbNumber, int offset, int readSize, String hostSuffix) {
        this.key = key;
        this.dbNumber = dbNumber;
        this.offset = offset;
        this.readSize = readSize;
        this.hostSuffix = hostSuffix;
    }

    public String getKey() {
        return key;
    }

    public int getDbNumber() {
        return dbNumber;
    }

    public int getOffset() {
        return offset;
    }

    public int getReadSize() {
        return readSize;
    }

    public String getHostSuffix() {
        return hostSuffix;
    }

    public String defaultIpForSubnet(String subnet) {
        return subnet + hostSuffix;
    }

    public static StationType fromKey(String key) {
        if (key == null) {
            return null;
        }
        for (StationType type : values()) {
            if (type.key.equalsIgnoreCase(key.trim())) {
                return type;
            }
        }
        return null;
    }
}
