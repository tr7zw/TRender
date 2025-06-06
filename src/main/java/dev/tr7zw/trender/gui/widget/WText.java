package dev.tr7zw.trender.gui.widget;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.Nullable;

import dev.tr7zw.trender.gui.client.LibGui;
import dev.tr7zw.trender.gui.client.RenderContext;
import dev.tr7zw.trender.gui.client.ScreenDrawing;
import dev.tr7zw.trender.gui.impl.client.TextAlignment;
import dev.tr7zw.trender.gui.impl.client.style.StyleConstants;
import dev.tr7zw.trender.gui.widget.data.HorizontalAlignment;
import dev.tr7zw.trender.gui.widget.data.InputResult;
import dev.tr7zw.trender.gui.widget.data.VerticalAlignment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
//#if MC >= 11800
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
//#endif
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

/**
 * A multiline label widget.
 *
 * @since 1.8.0
 */
public class WText extends WWidget {
    protected Component text;
    protected int color;
    protected Boolean drawShadows;
    protected HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
    protected VerticalAlignment verticalAlignment = VerticalAlignment.TOP;

    private List<FormattedCharSequence> wrappedLines;
    private boolean wrappingScheduled = false;

    public WText(Component text) {
        this(text, StyleConstants.DEFAULT_TEXT_COLOR);
    }

    public WText(Component text, int color) {
        this.text = Objects.requireNonNull(text, "text must not be null");
        this.color = color;
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(x, y);
        wrappingScheduled = true;
    }

    @Override
    public boolean canResize() {
        return true;
    }

    private void wrapLines() {
        Font font = Minecraft.getInstance().font;
        wrappedLines = font.split(text, getWidth());
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
        Font font = Minecraft.getInstance().font;
        int yOffset = TextAlignment.getTextOffsetY(verticalAlignment, getHeight(), wrappedLines.size());
        int lineIndex = (y - yOffset) / font.lineHeight;

        if (lineIndex >= 0 && lineIndex < wrappedLines.size()) {
            FormattedCharSequence line = wrappedLines.get(lineIndex);
            int xOffset = TextAlignment.getTextOffsetX(horizontalAlignment, getWidth(), line);
            return font.getSplitter().componentStyleAtWidth(line, x - xOffset);
        }

        return null;
    }

    @Override
    public void paint(RenderContext context, int x, int y, int mouseX, int mouseY) {
        if (wrappedLines == null || wrappingScheduled) {
            wrapLines();
            wrappingScheduled = false;
        }

        Font font = Minecraft.getInstance().font;
        int yOffset = TextAlignment.getTextOffsetY(verticalAlignment, getHeight(), wrappedLines.size());

        for (int i = 0; i < wrappedLines.size(); i++) {
            FormattedCharSequence line = wrappedLines.get(i);
            int c = color;

            if (getDrawShadows()) {
                ScreenDrawing.drawStringWithShadow(context, line, horizontalAlignment, x,
                        y + yOffset + i * font.lineHeight, getWidth(), c);
            } else {
                ScreenDrawing.drawString(context, line, horizontalAlignment, x, y + yOffset + i * font.lineHeight,
                        getWidth(), c);
            }
        }

        Style hoveredTextStyle = getTextStyleAt(mouseX, mouseY);
        ScreenDrawing.drawTextHover(context, hoveredTextStyle, x + mouseX, y + mouseY);
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        if (button != 0)
            return InputResult.IGNORED; // only left clicks

        Style hoveredTextStyle = getTextStyleAt(x, y);
        if (hoveredTextStyle != null) {
            boolean processed = Minecraft.getInstance().screen.handleComponentClicked(hoveredTextStyle);
            return InputResult.of(processed);
        }

        return InputResult.IGNORED;
    }

    /**
     * Gets the text of this text widget.
     *
     * @return the text
     */
    public Component getText() {
        return text;
    }

    /**
     * Sets the text of this text widget.
     *
     * @param text the new text
     * @return this text widget
     */
    public WText setText(Component text) {
        Objects.requireNonNull(text, "text is null");
        this.text = text;
        wrappingScheduled = true;

        return this;
    }

    /**
     * Gets the light mode color of this text widget.
     *
     * @return the color
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets the light mode color of this text widget.
     *
     * @param color the new color
     * @return this text widget
     */
    public WText setColor(int color) {
        this.color = color;
        return this;
    }

    /**
     * Checks whether shadows should be drawn for this text widget.
     * 
     * @return {@code true} shadows should be drawn, {@code false} otherwise
     * @since 11.1.0
     */
    public boolean getDrawShadows() {
        return drawShadows == null ? LibGui.getGuiStyle().isFontShadow() : drawShadows;
    }

    /**
     * Sets whether shadows should be drawn for this text widget.
     *
     * @param drawShadows {@code true} if shadows should be drawn, {@code false}
     *                    otherwise
     * @return this text widget
     * @since 11.1.0
     */
    public WText setDrawShadows(boolean drawShadows) {
        this.drawShadows = drawShadows;
        return this;
    }

    /**
     * Gets the horizontal alignment of this text widget.
     *
     * @return the alignment
     * @since 1.9.0
     */
    public HorizontalAlignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    /**
     * Sets the horizontal alignment of this text widget.
     *
     * @param horizontalAlignment the new alignment
     * @return this widget
     * @since 1.9.0
     */
    public WText setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        return this;
    }

    /**
     * Gets the vertical alignment of this text widget.
     *
     * @return the alignment
     * @since 2.0.0
     */
    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    /**
     * Sets the vertical alignment of this text widget.
     *
     * @param verticalAlignment the new alignment
     * @return this widget
     * @since 2.0.0
     */
    public WText setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
        return this;
    }

    //#if MC >= 11800
    @Override
    public void addNarrations(NarrationElementOutput builder) {
        builder.add(NarratedElementType.TITLE, text);
    }
    //#endif

}
