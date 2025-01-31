package dev.tr7zw.trender.gui.impl.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.trender.gui.client.CottonInventoryScreen;
import dev.tr7zw.trender.gui.client.RenderContext;

@Mixin(AbstractContainerScreen.class)
abstract class HandledScreenMixin {

    //#if NEOFORGE
    //$$private boolean addedHandler = false;
    //$$@Inject(method = "render", at = @At(value = "HEAD"))
    //$$private void onRender(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo info) {
    //$$    if (!addedHandler && (Object) this instanceof CottonInventoryScreen<?> cottonInventoryScreen) {
    //$$        ((AbstractContainerScreen)(Object)this).renderables.add((context2, mouseX2, mouseY2, delta2) -> {
    //$$            RenderContext renderContext = new RenderContext(context2);
    //$$            cottonInventoryScreen.paintDescription(renderContext, mouseX2, mouseY2, delta2);
    //$$        });
    //$$        addedHandler = true;
    //$$    }
    //$$ }
    //#else
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", shift = At.Shift.AFTER), allow = 1)
    private void onSuperRender(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if ((Object) this instanceof CottonInventoryScreen<?> cottonInventoryScreen) {
            RenderContext renderContext = new RenderContext(context);
            cottonInventoryScreen.paintDescription(renderContext, mouseX, mouseY, delta);
        }
    }
    //#endif
}
