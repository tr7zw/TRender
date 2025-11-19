package dev.tr7zw.trender.gui.widget;

import dev.tr7zw.trender.gui.client.*;
import dev.tr7zw.trender.gui.impl.client.*;
import dev.tr7zw.trender.gui.impl.client.TextAlignment;
import dev.tr7zw.trender.gui.impl.mixin.client.*;
import dev.tr7zw.trender.gui.widget.data.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.*;
import org.jetbrains.annotations.*;
//? if >= 1.18.0 {
import net.minecraft.client.gui.narration.*;
//? }

/**
 * A single-line label widget.
 */
public class WLabel extends WWidget {
    protected Component text;
    protected HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
    protected VerticalAlignment verticalAlignment = VerticalAlignment.TOP;
    protected Integer color = null;
    protected Boolean drawShadows;

    /**
     * Constructs a new label.
     *
     * @param text  the text of the label
     * @param color the color of the label
     */
    public WLabel(Component text, int color) {
        this.text = text;
        this.color = color;
    }

    /**
     * Constructs a new label with the {@linkplain #DEFAULT_TEXT_COLOR default text
     * color}.
     *
     * @param text the text of the label
     * @since 1.8.0
     */
    public WLabel(Component text) {
        this.text = text;
    }

    @Override
    public void paint(RenderContext context, int x, int y, int mouseX, int mouseY) {
        int yOffset = TextAlignment.getTextOffsetY(verticalAlignment, getHeight(), 1);

        if (getDrawShadows()) {
            ScreenDrawing.drawStringWithShadow(context, text.getVisualOrderText(), horizontalAlignment, x, y + yOffset,
                    this.getWidth(), getColor());
        } else {
            ScreenDrawing.drawString(context, text.getVisualOrderText(), horizontalAlignment, x, y + yOffset,
                    this.getWidth(), getColor());
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
                //? if >= 1.21.11 {
                ((ScreenAccessor) (Object) Minecraft.getInstance().screen).libgui$defaultHandleGameClickEvent(
                        hoveredTextStyle.getClickEvent(), Minecraft.getInstance(), Minecraft.getInstance().screen);
                return InputResult.PROCESSED;
                //? } else {
                /*
                return InputResult.of(screen.handleComponentClicked(hoveredTextStyle));
                *///? }
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
            int xOffset = TextAlignment.getTextOffsetX(horizontalAlignment, getWidth(), text.getVisualOrderText());
            //? if >= 1.21.11 {

            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            ActiveTextCollector.ClickableStyleFinder clickableStyleFinder = new ActiveTextCollector.ClickableStyleFinder(
                    font, x, y);
            minecraft.gui.getChat().captureClickableText(clickableStyleFinder,
                    minecraft.getWindow().getGuiScaledHeight(), minecraft.gui.getGuiTicks(), true);
            return clickableStyleFinder.result();
            //? } else {
            /*
            return Minecraft.getInstance().font.getSplitter().componentStyleAtWidth(text, x - xOffset);
            *///? }
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
     * Gets the light mode color of this label.
     *
     * @return the color
     */
    public int getColor() {
        return color != null ? color : LibGui.getGuiStyle().getTitleColor();
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
     * Checks whether shadows should be drawn for this label.
     * 
     * @return {@code true} shadows should be drawn, {@code false} otherwise
     * @since 11.1.0
     */
    public boolean getDrawShadows() {
        return drawShadows == null ? LibGui.getGuiStyle().isFontShadow() : drawShadows;
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

    //? if >= 1.18.0 {

    @Override
    public void addNarrations(NarrationElementOutput builder) {
        builder.add(NarratedElementType.TITLE, text);
    }
    //? }
}
