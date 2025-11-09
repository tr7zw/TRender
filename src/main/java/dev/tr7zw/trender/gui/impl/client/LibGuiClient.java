package dev.tr7zw.trender.gui.impl.client;

import dev.tr7zw.transition.config.*;
import dev.tr7zw.transition.loader.*;
import dev.tr7zw.transition.mc.*;
import dev.tr7zw.trender.gui.client.*;
import dev.tr7zw.trender.gui.impl.*;
import dev.tr7zw.trender.gui.impl.client.config.*;
import dev.tr7zw.trender.gui.impl.modmenu.*;
import org.apache.logging.log4j.*;
//? if fabric {
import net.fabricmc.api.*;
//? }

public class LibGuiClient
        //? if fabric {

        implements ClientModInitializer
//? }
{
    public static final Logger logger = LogManager.getLogger();
    public static volatile ConfigManager<LibGuiConfig> config = new ConfigManager<>("trender", LibGuiConfig::new, null);

    //    public static final Jankson jankson = JanksonFactory.createJankson();

    //? if fabric {

    @Override
    //? }
    public void onInitializeClient() {

        Proxy.proxy = new ClientProxy();
        ModLoaderUtil.disableDisplayTest();
        System.out.println("[LibGui] Initializing Client...");
        ModLoaderUtil.registerConfigScreen(
                (screen) -> new CottonClientScreen(ComponentProvider.literal("TRender"), new ConfigGui(screen)));
    }

}
