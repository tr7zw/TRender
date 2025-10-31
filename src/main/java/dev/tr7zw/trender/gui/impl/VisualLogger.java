package dev.tr7zw.trender.gui.impl;

import dev.tr7zw.transition.mc.*;
import dev.tr7zw.trender.gui.client.*;
import java.util.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.network.chat.*;
import net.minecraft.util.*;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.message.*;

/**
 * A "logger" that renders its messages on the screen in dev envs.
 */
public final class VisualLogger {
    private static final List<Component> WARNINGS = new ArrayList<>();

    private final Logger logger;
    private final Class<?> clazz;

    public VisualLogger(Class<?> clazz) {
        logger = LogManager.getLogger(clazz);
        this.clazz = clazz;
    }

    public void error(String message, Object... params) {
        log(message, params, Level.ERROR, ChatFormatting.RED);
    }

    public void warn(String message, Object... params) {
        log(message, params, Level.WARN, ChatFormatting.GOLD);
    }

    private void log(String message, Object[] params, Level level, ChatFormatting formatting) {
        logger.log(level, message, params);

        // TODO
        //        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
        var text = ComponentProvider.literal(clazz.getSimpleName() + '/');
        text.append(ComponentProvider.literal(level.name()).withStyle(formatting));
        text.append(ComponentProvider.literal(": " + ParameterizedMessage.format(message, params)));

        WARNINGS.add(text);
        //        }
    }

    public static void render(RenderContext context) {
        var client = Minecraft.getInstance();
        var textRenderer = client.font;
        int width = client.getWindow().getGuiScaledWidth();
        List<FormattedCharSequence> lines = new ArrayList<>();

        for (Component warning : WARNINGS) {
            lines.addAll(textRenderer.split(warning, width));
        }

        int fontHeight = textRenderer.lineHeight;
        int y = 0;

        for (var line : lines) {
            ScreenDrawing.coloredRect(context, 2, 2 + y, textRenderer.width(line), fontHeight, 0x88_000000);
            ScreenDrawing.drawString(context, line, 2, 2 + y, 0xFF_FFFFFF);
            y += fontHeight;
        }
    }

    public static void reset() {
        WARNINGS.clear();
    }
}
