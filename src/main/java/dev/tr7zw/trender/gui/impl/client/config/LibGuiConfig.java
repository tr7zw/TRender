package dev.tr7zw.trender.gui.impl.client.config;

import dev.tr7zw.trender.gui.impl.client.style.GuiStyle;

public class LibGuiConfig {

    //#if MC >= 12006
    public GuiStyle style = GuiStyle.VANILLA_MODERN;
    //#else
    //$$ public GuiStyle style = GuiStyle.VANILLA_OLD;
    //#endif
}
