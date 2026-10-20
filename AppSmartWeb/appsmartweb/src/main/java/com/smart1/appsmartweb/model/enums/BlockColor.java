package com.smart1.appsmartweb.model.enums;

/**
 * Codificação de cores de Bloco e Tampa (AGENTS.md Invariante #5):
 * 0 = Vazio/Livre, 1 = Preto, 2 = Vermelho, 3 = Azul.
 */
public enum BlockColor {
    NONE(0, "Vazio"),
    BLACK(1, "Preto"),
    RED(2, "Vermelho"),
    BLUE(3, "Azul");

    private final int code;
    private final String label;

    BlockColor(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static BlockColor fromCode(int code) {
        for (BlockColor color : values()) {
            if (color.code == code) {
                return color;
            }
        }
        return NONE;
    }
}
