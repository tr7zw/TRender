package dev.tr7zw.trender.gui.impl.client;

import dev.tr7zw.trender.gui.widget.data.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.util.*;

public final class TextAlignment {
    public static int getTextOffsetX(HorizontalAlignment alignment, int width, FormattedCharSequence text) {
        return switch (alignment) {
        case LEFT -> 0;

        case CENTER -> {
            Font renderer = Minecraft.getInstance().font;
            int textWidth = renderer.width(text);
            yield width / 2 - textWidth / 2;
        }

        case RIGHT -> {
            Font renderer = Minecraft.getInstance().font;
            int textWidth = renderer.width(text);
            yield width - textWidth;
        }
        };
    }

    public static int getTextOffsetY(VerticalAlignment alignment, int height, int lines) {
        return switch (alignment) {
        case TOP -> 0;

        case CENTER -> {
            Font renderer = Minecraft.getInstance().font;
            int textHeight = renderer.lineHeight * lines;
            yield height / 2 - textHeight / 2;
        }

        case BOTTOM -> {
            Font renderer = Minecraft.getInstance().font;
            int textHeight = renderer.lineHeight * lines;
            yield height - textHeight;
        }
        };
    }
}
