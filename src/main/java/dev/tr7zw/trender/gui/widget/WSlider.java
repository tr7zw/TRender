package dev.tr7zw.trender.gui.widget;

import org.jetbrains.annotations.Nullable;

import dev.tr7zw.trender.gui.client.BackgroundPainter;
import dev.tr7zw.trender.gui.client.RenderContext;
import dev.tr7zw.trender.gui.client.ScreenDrawing;
import dev.tr7zw.trender.gui.impl.client.style.WidgetTextures;
import dev.tr7zw.trender.gui.widget.data.Axis;
import net.minecraft.resources.ResourceLocation;

/**
 * A simple slider widget that can be used to select int values.
 *
 * @see WAbstractIntSlider for supported listeners
 */
public class WSlider extends WAbstractIntSlider {
    public static final int TRACK_WIDTH = 6;
    public static final int THUMB_SIZE = 8;

    @Nullable
    private BackgroundPainter backgroundPainter;

    public WSlider(int min, int max, Axis axis) {
        super(min, max, axis);
    }

    @Override
    protected int getThumbWidth() {
        return THUMB_SIZE;
    }

    @Override
    protected boolean isMouseInsideBounds(int x, int y) {
        // ao = axis-opposite mouse coordinate, aoCenter = center of ao's axis
        int ao = axis == Axis.HORIZONTAL ? y : x;
        int aoCenter = (axis == Axis.HORIZONTAL ? getHeight() : getWidth()) / 2;

        // Check if cursor is inside or <=2px away from track
        return ao >= aoCenter - TRACK_WIDTH / 2 - 2 && ao <= aoCenter + TRACK_WIDTH / 2 + 2;
    }

    @Override
    public void paint(RenderContext context, int x, int y, int mouseX, int mouseY) {
        if (backgroundPainter != null) {
            backgroundPainter.paintBackground(context, x, y, this);
        } else {
            float px = 1 / 32f;
            // thumbX/Y: thumb position in widget-space
            int thumbX, thumbY;
            // thumbXOffset: thumb texture x offset in pixels
            int thumbXOffset;
            ResourceLocation texture = WidgetTextures.getSliderTextures().get();

            if (axis == Axis.VERTICAL) {
                int trackX = x + getWidth() / 2 - TRACK_WIDTH / 2;
                thumbX = getWidth() / 2 - THUMB_SIZE / 2;
                thumbY = direction == Direction.UP
                        ? (getHeight() - THUMB_SIZE) + 1 - (int) (coordToValueRatio * (value - min))
                        : Math.round(coordToValueRatio * (value - min));
                thumbXOffset = 0;

                ScreenDrawing.texturedRect(context, trackX, y + 1, TRACK_WIDTH, 1, texture, 16 * px, 0 * px, 22 * px,
                        1 * px, 0xFFFFFFFF);
                ScreenDrawing.texturedRect(context, trackX, y + 2, TRACK_WIDTH, getHeight() - 2, texture, 16 * px,
                        1 * px, 22 * px, 2 * px, 0xFFFFFFFF);
                ScreenDrawing.texturedRect(context, trackX, y + getHeight(), TRACK_WIDTH, 1, texture, 16 * px, 2 * px,
                        22 * px, 3 * px, 0xFFFFFFFF);
            } else {
                int trackY = y + getHeight() / 2 - TRACK_WIDTH / 2;
                thumbX = direction == Direction.LEFT
                        ? (getWidth() - THUMB_SIZE) - (int) (coordToValueRatio * (value - min))
                        : Math.round(coordToValueRatio * (value - min));
                thumbY = getHeight() / 2 - THUMB_SIZE / 2;
                thumbXOffset = 8;

                ScreenDrawing.texturedRect(context, x, trackY, 1, TRACK_WIDTH, texture, 16 * px, 3 * px, 17 * px,
                        9 * px, 0xFFFFFFFF);
                ScreenDrawing.texturedRect(context, x + 1, trackY, getWidth() - 2, TRACK_WIDTH, texture, 17 * px,
                        3 * px, 18 * px, 9 * px, 0xFFFFFFFF);
                ScreenDrawing.texturedRect(context, x + getWidth() - 1, trackY, 1, TRACK_WIDTH, texture, 18 * px,
                        3 * px, 19 * px, 9 * px, 0xFFFFFFFF);
            }

            // thumbState values:
            // 0: default, 1: dragging, 2: hovered
            int thumbState = dragging ? 1
                    : (mouseX >= thumbX && mouseX <= thumbX + THUMB_SIZE && mouseY >= thumbY
                            && mouseY <= thumbY + THUMB_SIZE ? 2 : 0);
            ScreenDrawing.texturedRect(context, x + thumbX, y + thumbY, THUMB_SIZE, THUMB_SIZE, texture,
                    thumbXOffset * px, 0 * px + thumbState * 8 * px, (thumbXOffset + 8) * px,
                    8 * px + thumbState * 8 * px, 0xFFFFFFFF);

            if (thumbState == 0 && isFocused()) {
                ScreenDrawing.texturedRect(context, x + thumbX, y + thumbY, THUMB_SIZE, THUMB_SIZE, texture, 0 * px,
                        24 * px, 8 * px, 32 * px, 0xFFFFFFFF);
            }
        }
    }

    @Nullable
    public BackgroundPainter getBackgroundPainter() {
        return backgroundPainter;
    }

    public void setBackgroundPainter(@Nullable BackgroundPainter backgroundPainter) {
        this.backgroundPainter = backgroundPainter;
    }
}
