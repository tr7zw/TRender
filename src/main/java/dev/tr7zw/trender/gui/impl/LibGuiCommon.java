package dev.tr7zw.trender.gui.impl;

import dev.tr7zw.transition.mc.GeneralUtil;
import net.minecraft.resources.ResourceLocation;
//#if FABRIC
import net.fabricmc.api.ModInitializer;
//#endif

public final class LibGuiCommon
        //#if FABRIC
        implements ModInitializer
//#endif
{
    public static final String MOD_ID = "trender";

    public static ResourceLocation id(String path) {
        return GeneralUtil.getResourceLocation(MOD_ID, path);
    }

    //#if FABRIC
    @Override
    //#endif
    public void onInitialize() {

    }
}
