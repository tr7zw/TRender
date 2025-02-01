package dev.tr7zw.trender.gui.impl.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC >= 12000
@Mixin(GuiGraphics.class)
//#else
//$$@Mixin(MultiBufferSource.BufferSource.class)
//#endif
public interface DrawContextAccessor {
    //#if MC >= 12000
    @Accessor("bufferSource")
    MultiBufferSource.BufferSource libgui$getVertexConsumers();
    //#endif
}
