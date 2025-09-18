package dev.tr7zw.trender.gui.impl.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.tr7zw.transition.mc.ComponentProvider;
import dev.tr7zw.trender.gui.client.CottonClientScreen;
import dev.tr7zw.trender.gui.impl.modmenu.WeGotModMenuAtHome;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

@Mixin(TitleScreen.class)
public abstract class DevTitleScreenMixin extends Screen {

    protected DevTitleScreenMixin(Component title) {
        super(title);
    }

    //#if MC >= 12109
    @Inject(method = "createTestWorldButton", at = @At("RETURN"), cancellable = true)
    private void createTestWorldButton(int l, int rowHeight, CallbackInfoReturnable<Integer> ci) {
        this.addRenderableWidget(Button.builder(Component.literal("Open Test Screen"), (button) -> {
            CottonClientScreen screen = new CottonClientScreen(ComponentProvider.literal("We Got Mod Menu At Home"),
                    new WeGotModMenuAtHome()) {
                @Override
                public void onClose() {
                    this.minecraft.setScreen(this);
                }
            };
            this.minecraft.setScreen(screen);
        }).bounds(this.width / 2 + 2, l += rowHeight, 98, 20).build());
        System.out.println(this.width / 2 + 2 + " " + rowHeight);
        ci.setReturnValue(l);
    }
    //#endif

}
