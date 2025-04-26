package dev.tr7zw.trender.gui.impl.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 12000
@Mixin(GuiGraphics.class)
//#else
//$$@Mixin(net.minecraft.client.renderer.MultiBufferSource.BufferSource.class)
//#endif
public interface DrawContextAccessor {
    //#if MC < 12106
    //#if MC >= 12000
    //$$@org.spongepowered.asm.mixin.gen.Accessor("bufferSource")
    //$$ net.minecraft.client.renderer.MultiBufferSource.BufferSource libgui$getVertexConsumers();
    //#endif
    //#endif
}
