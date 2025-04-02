package dev.tr7zw.trender.gui.impl;

import dev.tr7zw.trender.gui.client.CottonClientScreen;
import dev.tr7zw.trender.gui.impl.modmenu.ConfigGui;
import dev.tr7zw.transition.loader.ModLoaderUtil;
import dev.tr7zw.transition.nms.ComponentProvider;
import dev.tr7zw.transition.nms.GeneralUtil;
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
        //        ScreenNetworkingImpl.init();
        ModLoaderUtil.registerConfigScreen((screen) -> new CottonClientScreen(
                ComponentProvider.translatable("options.libgui.libgui_settings"), new ConfigGui(screen)));
    }
}
