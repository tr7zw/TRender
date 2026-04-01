package dev.tr7zw.trender.gui.impl.client;

import net.minecraft.resources.*;

public record WidgetSprites(Identifier enabled, Identifier disabled, Identifier enabledFocused,
        Identifier disabledFocused) {
    public WidgetSprites(Identifier enabled, Identifier disabled) {
        this(enabled, enabled, disabled, disabled);
    }

    public WidgetSprites(Identifier enabled, Identifier disabled, Identifier enabledFocused) {
        this(enabled, disabled, enabledFocused, disabled);
    }

    public Identifier get(boolean enabled, boolean focused) {
        if (enabled) {
            return focused ? this.enabledFocused : this.enabled;
        } else {
            return focused ? this.disabledFocused : this.disabled;
        }
    }
}
