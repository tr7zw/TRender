package dev.tr7zw.trender.gui;

public enum TriState {
    TRUE, FALSE, DEFAULT;

    public boolean withDefault(boolean defaultVal) {
        return switch (this.ordinal()) {
        case 0 -> true;
        case 1 -> false;
        default -> defaultVal;
        };
    }
}
