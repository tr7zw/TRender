//package dev.tr7zw.trender.gui.impl.client;
//
//import org.jetbrains.annotations.Nullable;
//import org.joml.Matrix4f;
//
//import com.mojang.blaze3d.pipeline.RenderCall;
//import com.mojang.blaze3d.systems.RenderSystem;
//
//import dev.tr7zw.trender.gui.client.RenderContext;
//import dev.tr7zw.trender.gui.client.ScreenDrawing;
//import dev.tr7zw.trender.gui.impl.mixin.client.DrawContextAccessor;
//import net.minecraft.client.renderer.CompiledShaderProgram;
//import net.minecraft.resources.ResourceLocation;
//
///**
// * An implementation of LibNinePatch's {@link ContextualTextureRenderer} for
// * identifiers.
// */
//public enum NinePatchTextureRendererImpl /* implements ContextualTextureRenderer<ResourceLocation, GuiGraphics> */ {
//    INSTANCE;
//
//    //@Override
//    public void draw(ResourceLocation texture, RenderContext context, int x, int y, int width, int height, float u1,
//            float v1, float u2, float v2) {
//        ScreenDrawing.texturedRect(context, x, y, width, height, texture, u1, v1, u2, v2, 0xFF_FFFFFF);
//    }
//
//    //@Override
//    public void drawTiled(ResourceLocation texture, RenderContext context, int x, int y, int regionWidth,
//            int regionHeight, int tileWidth, int tileHeight, float u1, float v1, float u2, float v2) {
//        Matrix4f positionMatrix = context.pose().last().pose();
//        onRenderThread(() -> {
//            @Nullable
//            CompiledShaderProgram program = RenderSystem.getShader();
//            if (program != null) {
//                program.safeGetUniform("LibGuiRectanglePos").set((float) x, (float) y);
//                program.safeGetUniform("LibGuiTileDimensions").set((float) tileWidth, (float) tileHeight);
//                program.safeGetUniform("LibGuiTileUvs").setMat2x2(u1, v1, u2, v2);
//                program.safeGetUniform("LibGuiPositionMatrix").set(positionMatrix);
//            }
//        });
//
//        RenderSystem.setShaderColor(1, 1, 1, 1);
//        var renderLayer = LibGuiShaders.TILED_RECTANGLE_LAYER.apply(texture);
//        var buffer = context.getVertexConsumers().getBuffer(renderLayer);
//        buffer.addVertex(positionMatrix, x, y, 0);
//        buffer.addVertex(positionMatrix, x, y + regionHeight, 0);
//        buffer.addVertex(positionMatrix, x + regionWidth, y + regionHeight, 0);
//        buffer.addVertex(positionMatrix, x + regionWidth, y, 0);
//        context.flush();
//    }
//
//    private static void onRenderThread(RenderCall renderCall) {
//        if (RenderSystem.isOnRenderThread()) {
//            renderCall.execute();
//        } else {
//            RenderSystem.recordRenderCall(renderCall);
//        }
//    }
//}
