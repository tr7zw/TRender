package dev.tr7zw.trender.gui.impl;

import dev.tr7zw.transition.mc.GeneralUtil;
import net.minecraft.resources.ResourceLocation;
//? if fabric {

import net.fabricmc.api.ModInitializer;
//? }

public final class LibGuiCommon
//? if fabric {

        implements ModInitializer
//? }
{
    public static final String MOD_ID = "trender";

    public static ResourceLocation id(String path) {
        return GeneralUtil.getResourceLocation(MOD_ID, path);
    }

    //? if fabric {
    
    @Override
    //? }
    public void onInitialize() {

    }
}
