package com.smart1.appsmartweb.model.enums;

/**
 * Codificação de padrões de Lâmina (AGENTS.md Invariante #5):
 * 0 = Nenhum, 1 = Casa, 2 = Navio, 3 = Estrela.
 */
public enum LaminaPattern {
    NONE(0, "Nenhum"),
    HOUSE(1, "Casa"),
    SHIP(2, "Navio"),
    STAR(3, "Estrela");

    private final int code;
    private final String label;

    LaminaPattern(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static LaminaPattern fromCode(int code) {
        for (LaminaPattern pattern : values()) {
            if (pattern.code == code) {
                return pattern;
            }
        }
        return NONE;
    }
}
