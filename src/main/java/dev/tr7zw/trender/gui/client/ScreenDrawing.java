package dev.tr7zw.trender.gui.client;

import org.jetbrains.annotations.Nullable;
//#if MC < 12106
//$$ import net.minecraft.client.renderer.RenderType;
//#if MC >= 11904
//$$ import org.joml.Matrix4f;
//#else
//$$ import com.mojang.math.Matrix4f;
//#endif
//#endif

import dev.tr7zw.trender.gui.widget.data.HorizontalAlignment;
import dev.tr7zw.trender.gui.widget.data.Texture;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

//#if MC >= 12103
//#else
//$$ import com.mojang.blaze3d.systems.RenderSystem;
//$$ import com.mojang.blaze3d.vertex.BufferBuilder;
//$$ import com.mojang.blaze3d.vertex.Tesselator;
//$$ import net.minecraft.client.renderer.GameRenderer;
//$$ import com.mojang.blaze3d.vertex.VertexFormat;
//$$ import com.mojang.blaze3d.vertex.DefaultVertexFormat;
//$$ import com.mojang.blaze3d.vertex.BufferUploader;
//$$ import com.mojang.blaze3d.vertex.BufferBuilder;
//$$ import com.mojang.blaze3d.vertex.Tesselator;
//#endif

//#if MC <= 11605
//$$ import com.mojang.blaze3d.platform.GlStateManager;
//$$ import org.lwjgl.opengl.GL11;
//#endif

/**
 * {@code ScreenDrawing} contains utility methods for drawing contents on a
 * screen.
 */
public class ScreenDrawing {
    private ScreenDrawing() {
    }

    /**
     * Draws a textured rectangle.
     *
     * @param context the draw context
     * @param x       the x coordinate of the box on-screen
     * @param y       the y coordinate of the box on-screen
     * @param width   the width of the box on-screen
     * @param height  the height of the box on-screen
     * @param texture the Identifier for the texture
     * @param u1      the left edge of the texture
     * @param v1      the top edge of the texture
     * @param u2      the right edge of the texture
     * @param v2      the bottom edge of the texture
     * @param color   a color to tint the texture. This can be transparent! Use
     *                0xFF_FFFFFF if you don't want a color tint
     */
    public static void texturedRect(RenderContext context, int x, int y, int width, int height,
            ResourceLocation texture, float u1, float v1, float u2, float v2, int color) {
        texturedRect(context, x, y, width, height, texture, u1, v1, u2, v2, color, 1.0f, 64, 64);
    }

    /**
     * Draws a textured rectangle.
     *
     * @param context the draw context
     * @param x       the x coordinate of the box on-screen
     * @param y       the y coordinate of the box on-screen
     * @param width   the width of the box on-screen
     * @param height  the height of the box on-screen
     * @param texture the Identifier for the texture
     * @param u1      the left edge of the texture
     * @param v1      the top edge of the texture
     * @param u2      the right edge of the texture
     * @param v2      the bottom edge of the texture
     * @param color   a color to tint the texture. This can be transparent! Use
     *                0xFF_FFFFFF if you don't want a color tint
     */
    public static void texturedRect(RenderContext context, int x, int y, int width, int height,
            ResourceLocation texture, float u1, float v1, float u2, float v2, int color, int textureWidth,
            int textureHeight) {
        texturedRect(context, x, y, width, height, texture, u1, v1, u2, v2, color, 1.0f, textureWidth, textureHeight);
    }

    /**
     * Draws a textured rectangle.
     *
     * @param context the draw context
     * @param x       the x coordinate of the box on-screen
     * @param y       the y coordinate of the box on-screen
     * @param width   the width of the box on-screen
     * @param height  the height of the box on-screen
     * @param texture the texture
     * @param color   a color to tint the texture. This can be transparent! Use
     *                0xFF_FFFFFF if you don't want a color tint
     * @since 3.0.0
     */
    public static void texturedRect(RenderContext context, int x, int y, int width, int height, Texture texture,
            int color, int textureWidth, int textureHeight) {
        texturedRect(context, x, y, width, height, texture, color, 1.0f, textureWidth, textureHeight);
    }

    /**
     * Draws a textured rectangle.
     *
     * @param context the draw context
     * @param x       the x coordinate of the box on-screen
     * @param y       the y coordinate of the box on-screen
     * @param width   the width of the box on-screen
     * @param height  the height of the box on-screen
     * @param texture the texture
     * @param color   a color to tint the texture. This can be transparent! Use
     *                0xFF_FFFFFF if you don't want a color tint
     * @since 3.0.0
     */
    public static void texturedRect(RenderContext context, int x, int y, int width, int height, Texture texture,
            int color) {
        texturedRect(context, x, y, width, height, texture, color, 1.0f, 64, 64);
    }

    /**
     * Draws a textured rectangle.
     *
     * @param context the draw context
     * @param x       the x coordinate of the box on-screen
     * @param y       the y coordinate of the box on-screen
     * @param width   the width of the box on-screen
     * @param height  the height of the box on-screen
     * @param texture the texture
     * @param color   a color to tint the texture. This can be transparent! Use
     *                0xFF_FFFFFF if you don't want a color tint
     * @param opacity opacity of the drawn texture. (0f is fully opaque and 1f is
     *                fully visible)
     * @since 3.0.0
     */
    public static void texturedRect(RenderContext context, int x, int y, int width, int height, Texture texture,
            int color, float opacity, int textureWidth, int textureHeight) {
        switch (texture.type()) {
        // Standalone textures: convert into ID + UVs
        case STANDALONE -> texturedRect(context, x, y, width, height, texture.image(), texture.u1(), texture.v1(),
                texture.u2(), texture.v2(), color, opacity, textureWidth, textureHeight);

        // GUI sprites: Work more carefully as we need to support tiling/nine-slice
        case GUI_SPRITE -> {
            outer: if (texture.u1() == 0 && texture.u2() == 1 && texture.v1() == 0 && texture.v2() == 1) {
                // If we're drawing the full texture, just let vanilla do it.
                context.blitSprite(texture.image(), x, y, width, height, color);
            } else {
                // If we're only drawing a region, draw the full texture in a larger size and clip it
                // to only show the requested region.
                float fullWidth = width / Math.abs(texture.u2() - texture.u1());
                float fullHeight = height / Math.abs(texture.v2() - texture.v1());

                // u1 == u2 or v1 == v2, we don't care about these situations.
                if (Float.isInfinite(fullWidth) || Float.isInfinite(fullHeight))
                    break outer;

                // Calculate the offset left/top coordinates.
                float xo = x - fullWidth * Math.min(texture.u1(), texture.u2());
                float yo = y - fullHeight * Math.min(texture.v1(), texture.v2());

                context.pushPose();
                context.translate(xo, yo);

                // Note: scale instead of drawing a (fullWidth, fullHeight) rectangle so that edges of nine-slice
                // rectangles etc. are drawn scaled too. This matches the behavior of standalone textures.
                context.scale(fullWidth / width, fullHeight / height);

                // Clip to the wanted area on the screen...
                try (var frame = Scissors.push(context, x, y, width, height)) {
                    // ...and draw the texture.
                    context.blitSprite(texture.image(), 0, 0, width, height, color);
                }

                context.popPose();
            }
        }
        }
    }

    /**
     * Draws a textured rectangle.
     *
     * @param context the draw context
     * @param x       the x coordinate of the box on-screen
     * @param y       the y coordinate of the box on-screen
     * @param width   the width of the box on-screen
     * @param height  the height of the box on-screen
     * @param texture the Identifier for the texture
     * @param u1      the left edge of the texture
     * @param v1      the top edge of the texture
     * @param u2      the right edge of the texture
     * @param v2      the bottom edge of the texture
     * @param color   a color to tint the texture. This can be transparent! Use
     *                0xFF_FFFFFF if you don't want a color tint
     * @param opacity opacity of the drawn texture. (0f is fully opaque and 1f is
     *                fully visible)
     * @since 2.0.0
     */
    public static void texturedRect(RenderContext context, int x, int y, int width, int height,
            ResourceLocation texture, float u1, float v1, float u2, float v2, int color, float opacity,
            int textureWidth, int textureHeight) {
        if (width <= 0)
            width = 1;
        if (height <= 0)
            height = 1;
        //#if MC >= 12106
        float a = (color >> 24 & 255) / 255.0F;
        color = colorAtOpacity(color, a * opacity);
        // FIXME ?
        context.blit(texture, x, y, textureWidth * u1, textureHeight * v1, width, height, textureWidth, textureHeight);
        //                var buffer = context.getVertexConsumers().getBuffer(RenderType.entityTranslucent(texture));
        //                buffer.addVertex(model, x, y + height, 0).setUv(u1, v2).setColor(color);
        //                buffer.addVertex(model, x + width, y + height, 0).setUv(u2, v2).setColor(color);
        //                buffer.addVertex(model, x + width, y, 0).setUv(u2, v1).setColor(color);
        //                buffer.addVertex(model, x, y, 0).setUv(u1, v1).setColor(color);
        //#elseif MC >= 12103
        //$$  float a = (color >> 24 & 255) / 255.0F;
        //$$ color = colorAtOpacity(color, a * opacity);
        //$$ Matrix4f model = context.getPose().last().pose();
        //$$ var renderLayer = RenderType.guiTextured(texture);
        //$$ var buffer = context.getVertexConsumers().getBuffer(renderLayer);
        //$$ buffer.addVertex(model, x, y + height, 0).setUv(u1, v2).setColor(color);
        //$$ buffer.addVertex(model, x + width, y + height, 0).setUv(u2, v2).setColor(color);
        //$$ buffer.addVertex(model, x + width, y, 0).setUv(u2, v1).setColor(color);
        //$$ buffer.addVertex(model, x, y, 0).setUv(u1, v1).setColor(color);
        //#elseif MC >= 12100
        //$$ float r = (color >> 16 & 255) / 255.0F;
        //$$ float g = (color >> 8 & 255) / 255.0F;
        //$$ float b = (color & 255) / 255.0F;
        //$$ float a = (color >> 24 & 255) / 255.0F;
        //$$ Matrix4f model = context.getPose().last().pose();
        //$$ RenderSystem.enableBlend();
        //$$ RenderSystem.setShaderTexture(0, texture);
        //$$ RenderSystem.setShaderColor(r, g, b, opacity * a);
        //$$ RenderSystem.setShader(GameRenderer::getPositionTexShader);
        //$$ BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        //$$ buffer.addVertex(model, x,         y + height, 0).setUv(u1, v2);
        //$$ buffer.addVertex(model, x + width, y + height, 0).setUv(u2, v2);
        //$$ buffer.addVertex(model, x + width, y,          0).setUv(u2, v1);
        //$$ buffer.addVertex(model, x,         y,          0).setUv(u1, v1);
        //$$ BufferUploader.drawWithShader(buffer.build());
        //$$ RenderSystem.disableBlend();
        //$$ RenderSystem.setShaderColor(1, 1, 1, 1);
        //#elseif MC >= 11900
        //$$ float r = (color >> 16 & 255) / 255.0F;
        //$$ float g = (color >> 8 & 255) / 255.0F;
        //$$ float b = (color & 255) / 255.0F;
        //$$ float a = (color >> 24 & 255) / 255.0F;
        //$$ Matrix4f model = context.getPose().last().pose();
        //$$ RenderSystem.enableBlend();
        //$$ RenderSystem.setShaderTexture(0, texture);
        //$$ RenderSystem.setShaderColor(r, g, b, opacity * a);
        //$$ RenderSystem.setShader(GameRenderer::getPositionTexShader);
        //$$ BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        //$$ buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        //$$ buffer.vertex(model, x,         y + height, 0).uv(u1, v2).endVertex();
        //$$ buffer.vertex(model, x + width, y + height, 0).uv(u2, v2).endVertex();
        //$$ buffer.vertex(model, x + width, y,          0).uv(u2, v1).endVertex();
        //$$ buffer.vertex(model, x,         y,          0).uv(u1, v1).endVertex();
        //$$ BufferUploader.drawWithShader(buffer.end());
        //$$ RenderSystem.disableBlend();
        //$$ RenderSystem.setShaderColor(1, 1, 1, 1);
        //#elseif MC >= 11700
        //$$ float r = (color >> 16 & 255) / 255.0F;
        //$$ float g = (color >> 8 & 255) / 255.0F;
        //$$ float b = (color & 255) / 255.0F;
        //$$ float a = (color >> 24 & 255) / 255.0F;
        //$$ Matrix4f model = context.getPose().last().pose();
        //$$ RenderSystem.enableBlend();
        //$$ RenderSystem.setShaderTexture(0, texture);
        //$$ RenderSystem.setShaderColor(r, g, b, opacity * a);
        //$$ RenderSystem.setShader(GameRenderer::getPositionTexShader);
        //$$ BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        //$$ buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        //$$ buffer.vertex(model, x,         y + height, 0).uv(u1, v2).endVertex();
        //$$ buffer.vertex(model, x + width, y + height, 0).uv(u2, v2).endVertex();
        //$$ buffer.vertex(model, x + width, y,          0).uv(u2, v1).endVertex();
        //$$ buffer.vertex(model, x,         y,          0).uv(u1, v1).endVertex();
        //$$ buffer.end();
        //$$ BufferUploader.end(buffer);
        //$$ RenderSystem.disableBlend();
        //$$ RenderSystem.setShaderColor(1, 1, 1, 1);
        //#else
        //$$Minecraft.getInstance().getTextureManager().bind(texture);
        //$$if (width <= 0) width = 1;
        //$$if (height <= 0) height = 1;
        //$$float r = (color >> 16 & 255) / 255.0F;
        //$$float g = (color >> 8 & 255) / 255.0F;
        //$$float b = (color & 255) / 255.0F;
        //$$Tesselator tessellator = Tesselator.getInstance();
        //$$BufferBuilder buffer = tessellator.getBuilder();
        //$$RenderSystem.enableBlend();
        //$$RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        //$$buffer.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_COLOR_TEX); //I thought GL_QUADS was deprecated but okay, sure.
        //$$buffer.vertex(x,         y + height, 0).color(r, g, b, opacity).uv(u1, v2).endVertex();
        //$$buffer.vertex(x + width, y + height, 0).color(r, g, b, opacity).uv(u2, v2).endVertex();
        //$$buffer.vertex(x + width, y,          0).color(r, g, b, opacity).uv(u2, v1).endVertex();
        //$$buffer.vertex(x,         y,          0).color(r, g, b, opacity).uv(u1, v1).endVertex();
        //$$tessellator.end();
        //$$RenderSystem.disableBlend();
        //#endif
    }

    /**
     * Draws an untextured rectangle of the specified RGB color.
     */
    public static void coloredRect(RenderContext context, int left, int top, int width, int height, int color) {
        if (width <= 0)
            width = 1;
        if (height <= 0)
            height = 1;

        context.fill(left, top, left + width, top + height, color);
    }

    /**
     * Draws a beveled, round rectangle that is substantially similar to default
     * Minecraft UI panels.
     *
     * @param context the draw context
     * @param x       the X position of the panel
     * @param y       the Y position of the panel
     * @param width   the width of the panel
     * @param height  the height of the panel
     */
    public static void drawGuiPanel(RenderContext context, int x, int y, int width, int height) {
        if (LibGui.getGuiStyle().isDark())
            drawGuiPanel(context, x, y, width, height, 0xFF0B0B0B, 0xFF2F2F2F, 0xFF414141, 0xFF000000);
        else
            drawGuiPanel(context, x, y, width, height, 0xFF555555, 0xFFC6C6C6, 0xFFFFFFFF, 0xFF000000);
    }

    /**
     * Draws a beveled, round, and colored rectangle that is substantially similar
     * to default Minecraft UI panels.
     *
     * @param context    the draw context
     * @param x          the X position of the panel
     * @param y          the Y position of the panel
     * @param width      the width of the panel
     * @param height     the height of the panel
     * @param panelColor the panel ARGB color
     */
    public static void drawGuiPanel(RenderContext context, int x, int y, int width, int height, int panelColor) {
        int shadowColor = multiplyColor(panelColor, 0.50f);
        int hilightColor = multiplyColor(panelColor, 1.25f);

        drawGuiPanel(context, x, y, width, height, shadowColor, panelColor, hilightColor, 0xFF000000);
    }

    /**
     * Draws a beveled, round rectangle with custom edge colors that is
     * substantially similar to default Minecraft UI panels.
     *
     * @param context the draw context
     * @param x       the X position of the panel
     * @param y       the Y position of the panel
     * @param width   the width of the panel
     * @param height  the height of the panel
     * @param shadow  the bottom/right shadow ARGB color
     * @param panel   the center ARGB color
     * @param hilight the top/left hilight ARGB color
     * @param outline the outline ARGB color
     */
    public static void drawGuiPanel(RenderContext context, int x, int y, int width, int height, int shadow, int panel,
            int hilight, int outline) {
        coloredRect(context, x + 3, y + 3, width - 6, height - 6, panel); //Main panel area

        coloredRect(context, x + 2, y + 1, width - 4, 2, hilight); //Top hilight
        coloredRect(context, x + 2, y + height - 3, width - 4, 2, shadow); //Bottom shadow
        coloredRect(context, x + 1, y + 2, 2, height - 4, hilight); //Left hilight
        coloredRect(context, x + width - 3, y + 2, 2, height - 4, shadow); //Right shadow
        coloredRect(context, x + width - 3, y + 2, 1, 1, panel); //Topright non-hilight/non-shadow transition pixel
        coloredRect(context, x + 2, y + height - 3, 1, 1, panel); //Bottomleft non-hilight/non-shadow transition pixel
        coloredRect(context, x + 3, y + 3, 1, 1, hilight); //Topleft round hilight pixel
        coloredRect(context, x + width - 4, y + height - 4, 1, 1, shadow); //Bottomright round shadow pixel

        coloredRect(context, x + 2, y, width - 4, 1, outline); //Top outline
        coloredRect(context, x, y + 2, 1, height - 4, outline); //Left outline
        coloredRect(context, x + width - 1, y + 2, 1, height - 4, outline); //Right outline
        coloredRect(context, x + 2, y + height - 1, width - 4, 1, outline); //Bottom outline
        coloredRect(context, x + 1, y + 1, 1, 1, outline); //Topleft round pixel
        coloredRect(context, x + 1, y + height - 2, 1, 1, outline); //Bottomleft round pixel
        coloredRect(context, x + width - 2, y + 1, 1, 1, outline); //Topright round pixel
        coloredRect(context, x + width - 2, y + height - 2, 1, 1, outline); //Bottomright round pixel
    }

    /**
     * Draws a generalized-case beveled panel. Can be inset or outset depending on
     * arguments.
     *
     * @param context     the draw context
     * @param x           x coordinate of the topleft corner
     * @param y           y coordinate of the topleft corner
     * @param width       width of the panel
     * @param height      height of the panel
     * @param topleft     color of the top/left bevel
     * @param panel       color of the panel area
     * @param bottomright color of the bottom/right bevel
     */
    public static void drawBeveledPanel(RenderContext context, int x, int y, int width, int height, int topleft,
            int panel, int bottomright) {
        coloredRect(context, x, y, width, height, panel); //Center panel
        coloredRect(context, x, y, width - 1, 1, topleft); //Top shadow
        coloredRect(context, x, y + 1, 1, height - 2, topleft); //Left shadow
        coloredRect(context, x + width - 1, y + 1, 1, height - 1, bottomright); //Right hilight
        coloredRect(context, x + 1, y + height - 1, width - 1, 1, bottomright); //Bottom hilight
    }

    /**
     * Draws a string with a custom alignment.
     *
     * @param context the draw context
     * @param s       the string
     * @param align   the alignment of the string
     * @param x       the X position
     * @param y       the Y position
     * @param width   the width of the string, used for aligning
     * @param color   the text color
     */
    @Deprecated
    public static void drawString(RenderContext context, String s, HorizontalAlignment align, int x, int y, int width,
            int color) {
        var textRenderer = Minecraft.getInstance().font;
        switch (align) {
        case LEFT -> {
            context.drawString(textRenderer, s, x, y, color, false);
        }

        case CENTER -> {
            int wid = textRenderer.width(s);
            int l = (width / 2) - (wid / 2);
            context.drawString(textRenderer, s, x + l, y, color, false);
        }

        case RIGHT -> {
            int wid = textRenderer.width(s);
            int l = width - wid;
            context.drawString(textRenderer, s, x + l, y, color, false);
        }
        }
    }

    /**
     * Draws a text component with a custom alignment.
     *
     * @param context the draw context
     * @param text    the text
     * @param align   the alignment of the string
     * @param x       the X position
     * @param y       the Y position
     * @param width   the width of the string, used for aligning
     * @param color   the text color
     * @since 1.9.0
     */
    public static void drawString(RenderContext context, FormattedCharSequence text, HorizontalAlignment align, int x,
            int y, int width, int color) {
        var textRenderer = Minecraft.getInstance().font;
        switch (align) {
        case LEFT -> {
            context.drawString(textRenderer, text, x, y, color, false);
        }

        case CENTER -> {
            int wid = textRenderer.width(text);
            int l = (width / 2) - (wid / 2);
            context.drawString(textRenderer, text, x + l, y, color, false);
        }

        case RIGHT -> {
            int wid = textRenderer.width(text);
            int l = width - wid;
            context.drawString(textRenderer, text, x + l, y, color, false);
        }
        }
    }

    /**
     * Draws a shadowed string.
     *
     * @param context the draw context
     * @param s       the string
     * @param align   the alignment of the string
     * @param x       the X position
     * @param y       the Y position
     * @param width   the width of the string, used for aligning
     * @param color   the text color
     */
    public static void drawStringWithShadow(RenderContext context, String s, HorizontalAlignment align, int x, int y,
            int width, int color) {
        var textRenderer = Minecraft.getInstance().font;
        switch (align) {
        case LEFT -> {
            context.drawString(textRenderer, s, x, y, color, true);
        }

        case CENTER -> {
            int wid = textRenderer.width(s);
            int l = (width / 2) - (wid / 2);
            context.drawString(textRenderer, s, x + l, y, color, true);
        }

        case RIGHT -> {
            int wid = textRenderer.width(s);
            int l = width - wid;
            context.drawString(textRenderer, s, x + l, y, color, true);
        }
        }
    }

    /**
     * Draws a shadowed text component.
     *
     * @param context the draw context
     * @param text    the text component
     * @param align   the alignment of the string
     * @param x       the X position
     * @param y       the Y position
     * @param width   the width of the string, used for aligning
     * @param color   the text color
     */
    public static void drawStringWithShadow(RenderContext context, FormattedCharSequence text,
            HorizontalAlignment align, int x, int y, int width, int color) {
        var textRenderer = Minecraft.getInstance().font;
        switch (align) {
        case LEFT -> {
            context.drawString(textRenderer, text, x, y, color, true);
        }

        case CENTER -> {
            int wid = textRenderer.width(text);
            int l = (width / 2) - (wid / 2);
            context.drawString(textRenderer, text, x + l, y, color, true);
        }

        case RIGHT -> {
            int wid = textRenderer.width(text);
            int l = width - wid;
            context.drawString(textRenderer, text, x + l, y, color, true);
        }
        }
    }

    /**
     * Draws a left-aligned string.
     *
     * @param context the draw context
     * @param s       the string
     * @param x       the X position
     * @param y       the Y position
     * @param color   the text color
     */
    public static void drawString(RenderContext context, String s, int x, int y, int color) {
        context.drawString(Minecraft.getInstance().font, s, x, y, color, false);
    }

    /**
     * Draws a left-aligned text component.
     *
     * @param context the draw context
     * @param text    the text component
     * @param x       the X position
     * @param y       the Y position
     * @param color   the text color
     */
    public static void drawString(RenderContext context, FormattedCharSequence text, int x, int y, int color) {
        context.drawString(Minecraft.getInstance().font, text, x, y, color, false);
    }

    /**
     * Draws the text hover effects for a text style.
     *
     * <p>
     * This method has no effect when the caller is not in a LibGui screen. For
     * example, there will be nothing drawn in HUDs.
     *
     * @param context   the draw context
     * @param textStyle the text style
     * @param x         the X position
     * @param y         the Y position
     * @since 4.0.0
     */
    public static void drawTextHover(RenderContext context, @Nullable Style textStyle, int x, int y) {
        context.renderComponentHoverEffect(Minecraft.getInstance().font, textStyle, x, y);
    }

    public static int colorAtOpacity(int opaque, float opacity) {
        if (opacity < 0.0f)
            opacity = 0.0f;
        if (opacity > 1.0f)
            opacity = 1.0f;

        int a = (int) (opacity * 255.0f);

        return (opaque & 0xFFFFFF) | (a << 24);
    }

    public static int multiplyColor(int color, float amount) {
        int a = color & 0xFF000000;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;

        r = Math.min(r * amount, 1.0f);
        g = Math.min(g * amount, 1.0f);
        b = Math.min(b * amount, 1.0f);

        int ir = (int) (r * 255);
        int ig = (int) (g * 255);
        int ib = (int) (b * 255);

        return a | (ir << 16) | (ig << 8) | ib;
    }
}
