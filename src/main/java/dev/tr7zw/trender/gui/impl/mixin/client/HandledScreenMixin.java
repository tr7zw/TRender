package dev.tr7zw.trender.gui.impl.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.trender.gui.client.CottonInventoryScreen;
import dev.tr7zw.trender.gui.client.RenderContext;

//#if MC >= 12000
//#else
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//#endif

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
    //#if MC >= 12000
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", shift = At.Shift.AFTER), allow = 1)
    private void onSuperRender(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo info) {
        //#else
        //$$ @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V", shift = At.Shift.AFTER), allow = 1)
        //$$ private void onSuperRender(PoseStack poseStack, int mouseX, int mouseY, float delta, CallbackInfo info) {
        //#endif
        if ((Object) this instanceof CottonInventoryScreen<?> cottonInventoryScreen) {
            //#if MC >= 12000
            RenderContext renderContext = new RenderContext(context);
            //#else
            //$$ RenderContext renderContext = new RenderContext(((AbstractContainerScreen)(Object)this), poseStack);
            //#endif
            cottonInventoryScreen.paintDescription(renderContext, mouseX, mouseY, delta);
        }
    }
    //#endif
}
