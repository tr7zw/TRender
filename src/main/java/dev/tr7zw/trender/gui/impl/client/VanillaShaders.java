//#if MC < 12105
//$$package dev.tr7zw.trender.gui.impl.client;
//#if MC >= 12102
//$$import net.minecraft.client.renderer.CoreShaders;
//$$import net.minecraft.client.renderer.ShaderProgram;
//#elseif MC >= 11700
//$$ import java.util.function.Supplier;
//$$ import net.minecraft.client.renderer.ShaderInstance;
//$$ import net.minecraft.client.renderer.GameRenderer;
//#endif
//$$public final class VanillaShaders {
//#if MC >= 12102
//$$public static final ShaderProgram POSITION = CoreShaders.POSITION;
//#elseif MC >= 11700
//$$ public static final Supplier<ShaderInstance> POSITION = GameRenderer::getPositionShader;
//#endif
//$$}
//#endif