package dev.tr7zw.trender.gui.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;

import dev.tr7zw.trender.gui.client.RenderContext;
import dev.tr7zw.trender.gui.client.ScreenDrawing;
import dev.tr7zw.transition.mc.ComponentProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

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
