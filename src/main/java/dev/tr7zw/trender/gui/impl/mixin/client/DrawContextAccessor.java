package dev.tr7zw.trender.gui.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;

//? if >= 1.20.0 {

@Mixin(net.minecraft.client.gui.GuiGraphics.class)
//? } else {
/*
@Mixin(net.minecraft.client.renderer.MultiBufferSource.BufferSource.class)
*///? }
public interface DrawContextAccessor {
    //? if < 1.21.6 {
/*
    //? if >= 1.20.0 {
    
    @org.spongepowered.asm.mixin.gen.Accessor("bufferSource")
    net.minecraft.client.renderer.MultiBufferSource.BufferSource libgui$getVertexConsumers();
    //? }
       *///? }
}
