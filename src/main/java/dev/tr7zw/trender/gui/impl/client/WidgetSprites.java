package dev.tr7zw.trender.gui.impl.client;

import net.minecraft.resources.*;

public record WidgetSprites(/*? >= 1.21.11 {*/ Identifier /*?} else {*//* ResourceLocation *//*?}*/ enabled,
        /*? >= 1.21.11 {*/ Identifier /*?} else {*//* ResourceLocation *//*?}*/ disabled,
        /*? >= 1.21.11 {*/ Identifier /*?} else {*//* ResourceLocation *//*?}*/ enabledFocused,
        /*? >= 1.21.11 {*/ Identifier /*?} else {*//* ResourceLocation *//*?}*/ disabledFocused) {
    public WidgetSprites(/*? >= 1.21.11 {*/ Identifier /*?} else {*//* ResourceLocation *//*?}*/ enabled,
            /*? >= 1.21.11 {*/ Identifier /*?} else {*//* ResourceLocation *//*?}*/ disabled) {
        this(enabled, enabled, disabled, disabled);
    }

    public WidgetSprites(/*? >= 1.21.11 {*/ Identifier /*?} else {*//* ResourceLocation *//*?}*/ enabled,
            /*? >= 1.21.11 {*/ Identifier /*?} else {*//* ResourceLocation *//*?}*/ disabled,
            /*? >= 1.21.11 {*/ Identifier /*?} else {*//* ResourceLocation *//*?}*/ enabledFocused) {
        this(enabled, disabled, enabledFocused, disabled);
    }

    public /*? >= 1.21.11 {*/ Identifier /*?} else {*//* ResourceLocation *//*?}*/ get(boolean enabled,
            boolean focused) {
        if (enabled) {
            return focused ? this.enabledFocused : this.enabled;
        } else {
            return focused ? this.disabledFocused : this.disabled;
        }
    }
}
