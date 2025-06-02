package dev.tr7zw.trender.gui.client;

import dev.tr7zw.trender.gui.impl.client.LibGuiClient;
import dev.tr7zw.trender.gui.impl.client.style.GuiStyle;

/**
 * This class provides access to LibGui configuration and other global data.
 *
 * @since 4.0.0
 */
public final class LibGui {
    private LibGui() {
    }

    public static GuiStyle getGuiStyle() {
        return LibGuiClient.config.style;
    }
}
