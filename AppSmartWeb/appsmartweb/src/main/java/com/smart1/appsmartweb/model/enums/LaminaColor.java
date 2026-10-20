package com.smart1.appsmartweb.model.enums;

/**
 * Codificação de cores de Lâmina (AGENTS.md Invariante #5):
 * 0 = Nenhum, 1 = Vermelho, 2 = Azul, 3 = Amarelo, 4 = Verde, 5 = Preto, 6 = Branco.
 */
public enum LaminaColor {
    NONE(0, "Nenhum"),
    RED(1, "Vermelho"),
    BLUE(2, "Azul"),
    YELLOW(3, "Amarelo"),
    GREEN(4, "Verde"),
    BLACK(5, "Preto"),
    WHITE(6, "Branco");

    private final int code;
    private final String label;

    LaminaColor(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static LaminaColor fromCode(int code) {
        for (LaminaColor color : values()) {
            if (color.code == code) {
                return color;
            }
        }
        return NONE;
    }
}
