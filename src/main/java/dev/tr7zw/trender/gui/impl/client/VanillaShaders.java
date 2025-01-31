package dev.tr7zw.trender.gui.impl.client;

//#if MC >= 12102
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.ShaderProgram;
//#else
//$$ import java.util.function.Supplier;
//$$ import net.minecraft.client.renderer.ShaderInstance;
//$$ import net.minecraft.client.renderer.GameRenderer;
//#endif

public final class VanillaShaders {

    //#if MC >= 12102
    public static final ShaderProgram POSITION = CoreShaders.POSITION;
    //#else
    //$$ public static final Supplier<ShaderInstance> POSITION = GameRenderer::getPositionShader;
    //#endif

}
