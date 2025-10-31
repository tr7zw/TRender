package dev.tr7zw.trender.gui.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.transition.mc.ComponentProvider;
import dev.tr7zw.trender.gui.client.CottonClientScreen;
import dev.tr7zw.trender.gui.impl.modmenu.WeGotModMenuAtHome;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Mixin(PauseScreen.class)
public abstract class PauseMenuScreenMixin extends Screen {

    protected PauseMenuScreenMixin(Component title) {
        super(title);
    }

    //? if >= 1.21.6 {
    
    @Inject(method = "createPauseMenu", at = @At("RETURN"), cancellable = true)
    private void createTestWorldButton(CallbackInfo ci) {
        this.addRenderableWidget(Button.builder(Component.literal("Open Test Screen"), (button) -> {
            CottonClientScreen screen = new CottonClientScreen(ComponentProvider.literal("We Got Mod Menu At Home"),
                    new WeGotModMenuAtHome()) {
                @Override
                public void onClose() {
                    this.minecraft.setScreen(this);
                }
            };
            this.minecraft.setScreen(screen);
        }).bounds(5, 5, 98, 20).build());
    }
    //? }

}
