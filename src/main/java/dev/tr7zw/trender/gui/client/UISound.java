package dev.tr7zw.trender.gui.client;

import net.minecraft.client.*;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.sounds.*;

public class UISound {

    public static void playButtonClick() {
        //? if >=26.4 {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK));
        //? } else {
        /*
        Minecraft.getInstance().getSoundManager()
                .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         *///? }
    }

}
