package dev.tr7zw.trender.gui.widget;

import dev.tr7zw.trender.gui.client.*;
import dev.tr7zw.trender.gui.impl.client.*;
import dev.tr7zw.trender.gui.impl.client.style.*;
import dev.tr7zw.trender.gui.widget.data.*;
import java.util.function.*;

/**
 * Dynamic labels are labels that pull their text from a
 * {@code Supplier<String>}. They can be used for automatically getting data
 * from a block entity or another data source.
 *
 * <p>
 * Translating strings in dynamic labels should be done using
 * {@link net.minecraft.client.resources.language.I18n#get(String, Object...)}.
 */
public class WDynamicLabel extends WWidget {
    protected Supplier<String> text;
    protected HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
    protected VerticalAlignment verticalAlignment = VerticalAlignment.TOP;
    protected int color;
    protected boolean drawShadows;

    /**
     * Constructs a new dynamic label.
     *
     * @param text  the text of the label
     * @param color the color of the label
     */
    public WDynamicLabel(Supplier<String> text, int color) {
        this.text = text;
        this.color = color;
    }

    /**
     * Constructs a new dynamic label with the {@linkplain #DEFAULT_TEXT_COLOR
     * default text color}.
     *
     * @param text the text of the label
     */
    public WDynamicLabel(Supplier<String> text) {
        this(text, StyleConstants.DEFAULT_TEXT_COLOR);
    }

    @Override
    public void paint(RenderContext context, int x, int y, int mouseX, int mouseY) {
        int yOffset = TextAlignment.getTextOffsetY(verticalAlignment, getHeight(), 1);

        String tr = text.get();

        if (getDrawShadows()) {
            ScreenDrawing.drawStringWithShadow(context, tr, horizontalAlignment, x, y + yOffset, this.getWidth(),
                    color);
        } else {
            ScreenDrawing.drawString(context, tr, horizontalAlignment, x, y + yOffset, this.getWidth(), color);
        }
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(x, 20);
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
    public WDynamicLabel setDrawShadows(boolean drawShadows) {
        this.drawShadows = drawShadows;
        return this;
    }

    /**
     * Sets the text of this label.
     *
     * @param text the new text
     * @return this label
     */
    public WDynamicLabel setText(Supplier<String> text) {
        this.text = text;
        return this;
    }

    /**
     * Gets the horizontal text alignment of this label.
     *
     * @return the alignment
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
    public WDynamicLabel setHorizontalAlignment(HorizontalAlignment align) {
        this.horizontalAlignment = align;
        return this;
    }

    /**
     * Gets the vertical text alignment of this label.
     *
     * @return the alignment
     * @since 11.1.0
     */
    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    /**
     * Sets the vertical text alignment of this label.
     *
     * @param align the new text alignment
     * @return this label
     * @since 11.1.0
     */
    public WDynamicLabel setVerticalAlignment(VerticalAlignment align) {
        this.verticalAlignment = align;
        return this;
    }
}
