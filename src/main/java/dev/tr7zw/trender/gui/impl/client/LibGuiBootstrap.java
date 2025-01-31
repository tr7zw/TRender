//#if FORGE
//$$package dev.tr7zw.trender.gui.impl.client;
//$$
//$$import net.minecraftforge.api.distmarker.Dist;
//$$import net.minecraftforge.fml.DistExecutor;
//$$import net.minecraftforge.fml.common.Mod;
//$$import dev.tr7zw.trender.gui.impl.LibGuiCommon;
//$$
//$$@Mod("trender")
//$$public class LibGuiBootstrap {
//$$
//$$    public LibGuiBootstrap() {
//$$            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> { 
//$$         new LibGuiClient().onInitializeClient();
//$$        });
//$$        new LibGuiCommon().onInitialize();
//$$    }
//$$    
//$$}
//#elseif NEOFORGE
//$$package dev.tr7zw.trender.gui.impl.client;
//$$
//$$import net.neoforged.api.distmarker.Dist;
//$$import net.neoforged.fml.loading.FMLEnvironment;
//$$import net.neoforged.fml.common.Mod;
//$$import dev.tr7zw.trender.gui.impl.LibGuiCommon;
//$$
//$$@Mod("trender")
//$$public class LibGuiBootstrap {
//$$
//$$    public LibGuiBootstrap() {
//$$        if(FMLEnvironment.dist == Dist.CLIENT) {
//$$         new LibGuiClient().onInitializeClient();
//$$        }
//$$        new LibGuiCommon().onInitialize();
//$$    }
//$$    
//$$}
//#endif
