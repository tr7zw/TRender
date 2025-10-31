//? if < 1.21.5 {
/*
package dev.tr7zw.trender.gui.impl.client;
//? if >= 1.21.2 {


  import net.minecraft.client.renderer.CoreShaders;
  import net.minecraft.client.renderer.ShaderProgram;
 //? } else if >= 1.17.0 {
/^
import java.util.function.Supplier;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.GameRenderer;

^///? }
public final class VanillaShaders {
    //? if >= 1.21.2 {
    
      public static final ShaderProgram POSITION = CoreShaders.POSITION;
     //? } else if >= 1.17.0 {
/^
    public static final Supplier<ShaderInstance> POSITION = GameRenderer::getPositionShader;
    ^///? }
}
*///? }
