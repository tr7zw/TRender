package dev.tr7zw.trender.gui.impl.client.style;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GuiStyle {
MODERN("modern", false, StyleConstants.DEFAULT_TEXT_COLOR, false),
MODERN_DARK("modern_dark", true, StyleConstants.DEFAULT_DARKMODE_TEXT_COLOR, true),
VANILLA_OLD("vanilla_old", false, 0xFFFFFFFF, true),
VANILLA_MODERN("vanilla_modern", true, StyleConstants.DEFAULT_DARKMODE_TEXT_COLOR, true),
;
    
    private final String prefix;
    private final boolean dark;
    private final int titleColor;
    private final boolean fontShadow;
    
}
