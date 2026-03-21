package dev.tr7zw.trender.gui.impl.client;

import dev.tr7zw.transition.config.*;
import dev.tr7zw.transition.loader.*;
import dev.tr7zw.transition.manager.*;
import dev.tr7zw.transition.mc.*;
import dev.tr7zw.trender.gui.client.*;
import dev.tr7zw.trender.gui.impl.*;
import dev.tr7zw.trender.gui.impl.client.config.*;
import dev.tr7zw.trender.gui.impl.modmenu.*;
import net.minecraft.client.*;
import org.apache.logging.log4j.*;
//? if fabric {
import net.fabricmc.api.*;

import java.util.*;
//? }

public class LibGuiClient
        //? if fabric {

        implements ClientModInitializer
//? }
{
    public static final Logger logger = LogManager.getLogger();
    public static volatile ConfigManager<LibGuiConfig> config = new ConfigManager<>("trender", LibGuiConfig::new, null);

    //? if fabric {

    @Override
    //? }
    public void onInitializeClient() {

        Proxy.proxy = new ClientProxy();
        ModLoaderUtil.disableDisplayTest();
        System.out.println("[LibGui] Initializing Client...");
        ModLoaderUtil.registerConfigScreen(
                (screen) -> new CottonClientScreen(ComponentProvider.literal("TRender"), new ConfigGui(screen)));
        Runnable openSettings = () -> {
            CottonClientScreen screen = new CottonClientScreen(ComponentProvider.literal("We Got Mod Menu At Home"),
                    new WeGotModMenuAtHome()) {
                @Override
                public void onClose() {
                    this.minecraft.setScreen(null);
                }
            };
            Minecraft.getInstance().setScreen(screen);
        };
        CodeManager.getInstance().registerCode(new String(Base64.getDecoder().decode("c2V0dGluZ3MgcGxz")), openSettings);
        CodeManager.getInstance().registerCode(new String(Base64.getDecoder().decode("aW5zdGFsbCBtb2RtZW51")), openSettings);
    }

}
