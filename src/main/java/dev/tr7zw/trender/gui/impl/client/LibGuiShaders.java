package dev.tr7zw.trender.gui.impl.client;

import net.minecraft.Util;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.client.renderer.ShaderProgram;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import dev.tr7zw.trender.gui.impl.LibGuiCommon;

import java.util.function.Function;

public final class LibGuiShaders {
    public static final ShaderProgram TILED_RECTANGLE = new ShaderProgram(LibGuiCommon.id("core/tiled_rectangle"),
            DefaultVertexFormat.POSITION, ShaderDefines.EMPTY);

    public static final Function<ResourceLocation, RenderType> TILED_RECTANGLE_LAYER = Util
            .memoize(texture -> RenderType.create("trender:tiled_gui_rectangle", DefaultVertexFormat.POSITION,
                    VertexFormat.Mode.QUADS, RenderType.SMALL_BUFFER_SIZE,
                    RenderType.CompositeState.builder()
                            .setTextureState(new RenderStateShard.TextureStateShard(texture, TriState.FALSE, false))
                            .setShaderState(new RenderStateShard.ShaderStateShard(TILED_RECTANGLE))
                            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                            .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST).createCompositeState(false)));

    static void register() {
        // Register our core shaders.
        // The tiled rectangle shader is used for performant tiled texture rendering.
        CoreShaders.getProgramsToPreload().add(TILED_RECTANGLE);
    }
}
