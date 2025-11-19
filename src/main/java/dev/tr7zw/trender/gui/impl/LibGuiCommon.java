package dev.tr7zw.trender.gui.impl;

import dev.tr7zw.transition.mc.*;
import net.minecraft.resources.*;
//? if fabric {
import net.fabricmc.api.*;
//? }

public final class LibGuiCommon
        //? if fabric {

        implements ModInitializer
//? }
{
    public static final String MOD_ID = "trender";

    public static /*? >= 1.21.11 {*/ Identifier /*?} else {*//* ResourceLocation *//*?}*/ id(String path) {
        return GeneralUtil.getResourceLocation(MOD_ID, path);
    }

    //? if fabric {

    @Override
    //? }
    public void onInitialize() {

    }
}
