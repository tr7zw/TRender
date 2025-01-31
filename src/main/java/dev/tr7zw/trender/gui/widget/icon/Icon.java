package dev.tr7zw.trender.gui.widget.icon;

import dev.tr7zw.trender.gui.client.RenderContext;

/**
 * A square icon for a widget such as a button.
 *
 * @see ItemIcon
 * @see TextureIcon
 * @since 2.2.0
 */
public interface Icon {
    /**
     * Paints this icon.
     *
     * @param context the draw context
     * @param x       the X coordinate
     * @param y       the Y coordinate
     * @param size    the size of this icon in pixels (size N means a N*N square)
     */
    void paint(RenderContext context, int x, int y, int size);
}
