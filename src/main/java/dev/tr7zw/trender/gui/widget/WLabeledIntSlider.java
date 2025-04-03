package dev.tr7zw.trender.gui.widget;

import org.jetbrains.annotations.Nullable;

import dev.tr7zw.trender.gui.client.RenderContext;
import dev.tr7zw.trender.gui.client.ScreenDrawing;
import dev.tr7zw.trender.gui.impl.client.NarrationMessages;
import dev.tr7zw.trender.gui.impl.client.WidgetTextures;
import dev.tr7zw.trender.gui.widget.data.Axis;
import dev.tr7zw.trender.gui.widget.data.HorizontalAlignment;
import dev.tr7zw.transition.mc.ComponentProvider;
import dev.tr7zw.transition.mc.MathUtil;
//#if MC >= 11800
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
//#endif
import net.minecraft.network.chat.Component;

/**
 * A vanilla-style labeled slider widget.
 *
 * <p>
 * In addition to the standard slider listeners, labeled sliders also support
 * "label updaters" that can update the label when the value is changed.
 *
 * @see WAbstractIntSlider for more information about listeners
 */
public class WLabeledIntSlider extends WAbstractIntSlider {
    @Nullable
    private Component label = null;
    @Nullable
    private LabelUpdater labelUpdater = null;
    private HorizontalAlignment labelAlignment = HorizontalAlignment.CENTER;

    /**
     * Constructs a horizontal slider with no default label.
     *
     * @param min the minimum value
     * @param max the maximum value
     */
    public WLabeledIntSlider(int min, int max) {
        this(min, max, Axis.HORIZONTAL);
    }

    /**
     * Constructs a slider with no default label.
     *
     * @param min  the minimum value
     * @param max  the maximum value
     * @param axis the slider axis
     */
    public WLabeledIntSlider(int min, int max, Axis axis) {
        super(min, max, axis);
    }

    /**
     * Constructs a slider.
     *
     * @param min   the minimum value
     * @param max   the maximum value
     * @param axis  the slider axis
     * @param label the slider label (can be null)
     */
    public WLabeledIntSlider(int min, int max, Axis axis, @Nullable Component label) {
        this(min, max, axis);
        this.label = label;
    }

    /**
     * Constructs a horizontal slider.
     *
     * @param min   the minimum value
     * @param max   the maximum value
     * @param label the slider label (can be null)
     */
    public WLabeledIntSlider(int min, int max, @Nullable Component label) {
        this(min, max);
        this.label = label;
    }

    /**
     * Gets the current label of this slider.
     *
     * @return the label
     */
    @Nullable
    public Component getLabel() {
        return label;
    }

    /**
     * Sets the label of this slider.
     *
     * @param label the new label
     */
    public void setLabel(@Nullable Component label) {
        this.label = label;
    }

    @Override
    protected void onValueChanged(int value) {
        super.onValueChanged(value);
        if (labelUpdater != null) {
            label = labelUpdater.updateLabel(value);
        }
    }

    /**
     * Gets the text alignment of this slider's label.
     *
     * @return the alignment
     */
    public HorizontalAlignment getLabelAlignment() {
        return labelAlignment;
    }

    /**
     * Sets the text alignment of this slider's label.
     *
     * @param labelAlignment the new alignment
     */
    public void setLabelAlignment(HorizontalAlignment labelAlignment) {
        this.labelAlignment = labelAlignment;
    }

    /**
     * Gets the {@link LabelUpdater} of this slider.
     *
     * @return the label updater
     */
    @Nullable
    public LabelUpdater getLabelUpdater() {
        return labelUpdater;
    }

    /**
     * Sets the {@link LabelUpdater} of this slider.
     *
     * @param labelUpdater the new label updater
     */
    public void setLabelUpdater(@Nullable LabelUpdater labelUpdater) {
        this.labelUpdater = labelUpdater;
        if (labelUpdater != null) {
            label = labelUpdater.updateLabel(value);
        }
    }

    @Override
    protected int getThumbWidth() {
        return 8;
    }

    @Override
    protected boolean isMouseInsideBounds(int x, int y) {
        return x >= 0 && x <= getWidth() && y >= 0 && y <= getHeight();
    }

    @Override
    public void paint(RenderContext context, int x, int y, int mouseX, int mouseY) {
        int aWidth = axis == Axis.HORIZONTAL ? getWidth() : getHeight();
        int aHeight = axis == Axis.HORIZONTAL ? getHeight() : getWidth();
        int rotMouseX = axis == Axis.HORIZONTAL ? (direction == Direction.LEFT ? getWidth() - mouseX : mouseX)
                : (direction == Direction.UP ? getHeight() - mouseY : mouseY);
        int rotMouseY = axis == Axis.HORIZONTAL ? mouseY : mouseX;

        var matrices = context.pose();
        matrices.pushPose();
        matrices.translate(x, y, 0);
        if (axis == Axis.VERTICAL) {
            matrices.translate(0, getHeight(), 0);
            matrices.mulPose(MathUtil.ZP.rotationDegrees(270));
        }
        var background = WidgetTextures.getSLIDER();
        context.blitSprite(this.isFocused() ? background.enabledFocused() : background.enabled(), 0, 0, aWidth, aHeight,
                4, 4, 16, 16);

        int thumbX = Math.round(coordToValueRatio * (value - min));
        int thumbY = 0;
        int thumbWidth = getThumbWidth();
        int thumbHeight = aHeight;
        boolean hovering = rotMouseX >= thumbX && rotMouseX <= thumbX + thumbWidth && rotMouseY >= thumbY
                && rotMouseY <= thumbY + thumbHeight;

        var thumbTextures = WidgetTextures.getLabeledSliderHandleTextures(shouldRenderInDarkMode());
        var thumbTexture = thumbTextures.get(true, dragging || hovering);
        context.blitSprite(thumbTexture, thumbX, thumbY, thumbWidth, thumbHeight, 2, 2, 8, 20);

        if (label != null) {
            int color = isMouseInsideBounds(mouseX, mouseY) ? 0xFFFFA0 : 0xE0E0E0;
            ScreenDrawing.drawStringWithShadow(context, label.getVisualOrderText(), labelAlignment, 2, aHeight / 2 - 4,
                    aWidth - 4, color);
        }
        matrices.popPose();
    }

    //#if MC >= 11800
    @Override
    public void addNarrations(NarrationElementOutput builder) {
        if (getLabel() != null) {
            builder.add(NarratedElementType.TITLE, ComponentProvider
                    .translatable(NarrationMessages.LABELED_SLIDER_TITLE_KEY, getLabel(), value, min, max));
            builder.add(NarratedElementType.USAGE, NarrationMessages.SLIDER_USAGE);
        } else {
            super.addNarrations(builder);
        }
    }
    //#endif

    /**
     * A label updater updates the label of a slider based on the current value.
     *
     * <p>
     * Useful for situations when you want to have display values on the slider.
     */
    @FunctionalInterface
    public interface LabelUpdater {
        /**
         * Gets the updated label for the new slider value.
         *
         * @param value the slider value
         * @return the label
         */
        Component updateLabel(int value);
    }
}
