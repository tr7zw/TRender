package dev.tr7zw.trender.gui.widget.icon;

import dev.tr7zw.trender.gui.client.RenderContext;
import dev.tr7zw.trender.gui.client.ScreenDrawing;
import dev.tr7zw.trender.gui.widget.data.Texture;
import net.minecraft.resources.ResourceLocation;

/**
 * An icon that draws a texture.
 *
 * @since 2.2.0
 */
public class TextureIcon implements Icon {
    private final Texture texture;
    private final int width;
    private final int height;
    private float opacity = 1f;
    private int color = 0xFF_FFFFFF;

    /**
     * Constructs a new texture icon.
     *
     * @param texture the identifier of the icon texture
     */
    public TextureIcon(ResourceLocation texture, int width, int height) {
        this(new Texture(texture), width, height);
    }

    /**
     * Constructs a new texture icon.
     *
     * @param texture the identifier of the icon texture
     * @since 3.0.0
     */
    public TextureIcon(Texture texture, int width, int height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the opacity of the texture.
     *
     * @return the opacity
     */
    public float getOpacity() {
        return opacity;
    }

    /**
     * Sets the opacity of the texture.
     *
     * @param opacity the new opacity between 0 (fully transparent) and 1 (fully
     *                opaque)
     * @return this icon
     */
    public TextureIcon setOpacity(float opacity) {
        this.opacity = opacity;
        return this;
    }

    /**
     * Gets the color tint of the texture.
     *
     * @return the color tint
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets the color tint of the texture.
     *
     * @param color the new color tint
     * @return this icon
     */
    public TextureIcon setColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    public void paint(RenderContext context, int x, int y, int size) {
        ScreenDrawing.texturedRect(context, x, y, size, size, texture, color, opacity, width, height);
    }
}
