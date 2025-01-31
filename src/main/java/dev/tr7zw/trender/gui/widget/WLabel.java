package dev.tr7zw.trender.gui.widget;

import org.jetbrains.annotations.Nullable;

import dev.tr7zw.trender.gui.client.LibGui;
import dev.tr7zw.trender.gui.client.RenderContext;
import dev.tr7zw.trender.gui.client.ScreenDrawing;
import dev.tr7zw.trender.gui.impl.client.TextAlignment;
import dev.tr7zw.trender.gui.widget.data.HorizontalAlignment;
import dev.tr7zw.trender.gui.widget.data.InputResult;
import dev.tr7zw.trender.gui.widget.data.VerticalAlignment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

/**
 * A single-line label widget.
 */
public class WLabel extends WWidget {
    protected Component text;
    protected HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
    protected VerticalAlignment verticalAlignment = VerticalAlignment.TOP;
    protected int color;
    protected int darkmodeColor;
    protected boolean drawShadows;

    /**
     * The default text color for light mode labels.
     */
    public static final int DEFAULT_TEXT_COLOR = 0x404040;

    /**
     * The default text color for {@linkplain LibGui#isDarkMode() dark mode} labels.
     */
    public static final int DEFAULT_DARKMODE_TEXT_COLOR = 0xbcbcbc;

    /**
     * Constructs a new label.
     *
     * @param text  the text of the label
     * @param color the color of the label
     */
    public WLabel(Component text, int color) {
        this.text = text;
        this.color = color;
        this.darkmodeColor = (color == DEFAULT_TEXT_COLOR) ? DEFAULT_DARKMODE_TEXT_COLOR : color;
    }

    /**
     * Constructs a new label with the {@linkplain #DEFAULT_TEXT_COLOR default text
     * color}.
     *
     * @param text the text of the label
     * @since 1.8.0
     */
    public WLabel(Component text) {
        this(text, DEFAULT_TEXT_COLOR);
    }

    @Override
    public void paint(RenderContext context, int x, int y, int mouseX, int mouseY) {
        int yOffset = TextAlignment.getTextOffsetY(verticalAlignment, height, 1);

        if (getDrawShadows()) {
            ScreenDrawing.drawStringWithShadow(context, text.getVisualOrderText(), horizontalAlignment, x, y + yOffset,
                    this.getWidth(), shouldRenderInDarkMode() ? darkmodeColor : color);
        } else {
            ScreenDrawing.drawString(context, text.getVisualOrderText(), horizontalAlignment, x, y + yOffset,
                    this.getWidth(), shouldRenderInDarkMode() ? darkmodeColor : color);
        }

        Style hoveredTextStyle = getTextStyleAt(mouseX, mouseY);
        ScreenDrawing.drawTextHover(context, hoveredTextStyle, x + mouseX, y + mouseY);
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        Style hoveredTextStyle = getTextStyleAt(x, y);
        if (hoveredTextStyle != null) {
            Screen screen = Minecraft.getInstance().screen;
            if (screen != null) {
                return InputResult.of(screen.handleComponentClicked(hoveredTextStyle));
            }
        }

        return InputResult.IGNORED;
    }

    /**
     * Gets the text style at the specific widget-space coordinates.
     *
     * @param x the X coordinate in widget space
     * @param y the Y coordinate in widget space
     * @return the text style at the position, or null if not found
     */

    @Nullable
    public Style getTextStyleAt(int x, int y) {
        if (isWithinBounds(x, y)) {
            int xOffset = TextAlignment.getTextOffsetX(horizontalAlignment, width, text.getVisualOrderText());
            return Minecraft.getInstance().font.getSplitter().componentStyleAtWidth(text, x - xOffset);
        }
        return null;
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(x, Math.max(8, y));
    }

    /**
     * Gets the dark mode color of this label.
     *
     * @return the color
     * @since 2.0.0
     */
    public int getDarkmodeColor() {
        return darkmodeColor;
    }

    /**
     * Sets the dark mode color of this label.
     *
     * @param color the new color
     * @return this label
     */
    public WLabel setDarkmodeColor(int color) {
        darkmodeColor = color;
        return this;
    }

    /**
     * Disables separate dark mode coloring by copying the dark color to be the
     * light color.
     *
     * @return this label
     */
    public WLabel disableDarkmode() {
        this.darkmodeColor = this.color;
        return this;
    }

    /**
     * Gets the light mode color of this label.
     *
     * @return the color
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets the light mode color of this label.
     *
     * @param color the new color
     * @return this label
     */
    public WLabel setColor(int color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the light and dark mode colors of this label.
     *
     * @param color         the new light color
     * @param darkmodeColor the new dark color
     * @return this label
     */
    public WLabel setColor(int color, int darkmodeColor) {
        this.color = color;
        this.darkmodeColor = darkmodeColor;
        return this;
    }

    /**
     * Checks whether shadows should be drawn for this label.
     * 
     * @return {@code true} shadows should be drawn, {@code false} otherwise
     * @since 11.1.0
     */
    public boolean getDrawShadows() {
        return drawShadows;
    }

    /**
     * Sets whether shadows should be drawn for this label.
     *
     * @param drawShadows {@code true} if shadows should be drawn, {@code false}
     *                    otherwise
     * @return this label
     * @since 11.1.0
     */
    public WLabel setDrawShadows(boolean drawShadows) {
        this.drawShadows = drawShadows;
        return this;
    }

    /**
     * Gets the text of this label.
     *
     * @return the text
     */
    public Component getText() {
        return text;
    }

    /**
     * Sets the text of this label.
     *
     * @param text the new text
     * @return this label
     */
    public WLabel setText(Component text) {
        this.text = text;
        return this;
    }

    /**
     * Gets the horizontal text alignment of this label.
     *
     * @return the alignment
     * @since 2.0.0
     */
    public HorizontalAlignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    /**
     * Sets the horizontal text alignment of this label.
     *
     * @param align the new text alignment
     * @return this label
     */
    public WLabel setHorizontalAlignment(HorizontalAlignment align) {
        this.horizontalAlignment = align;
        return this;
    }

    /**
     * Gets the vertical text alignment of this label.
     *
     * @return the alignment
     * @since 2.0.0
     */
    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    /**
     * Sets the vertical text alignment of this label.
     *
     * @param align the new text alignment
     * @return this label
     */
    public WLabel setVerticalAlignment(VerticalAlignment align) {
        this.verticalAlignment = align;
        return this;
    }

    @Override
    public void addNarrations(NarrationElementOutput builder) {
        builder.add(NarratedElementType.TITLE, text);
    }
}
