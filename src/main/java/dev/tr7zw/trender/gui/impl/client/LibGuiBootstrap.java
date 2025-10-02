//#if FORGE
//$$package dev.tr7zw.trender.gui.impl.client;
//$$
//$$import net.minecraftforge.api.distmarker.Dist;
//$$import net.minecraftforge.fml.DistExecutor;
//$$import net.minecraftforge.fml.common.Mod;
//$$import dev.tr7zw.trender.gui.impl.LibGuiCommon;
//$$import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//$$import dev.tr7zw.transition.loader.ModLoaderUtil;
//$$
//$$@Mod("trender")
//$$public class LibGuiBootstrap {
//$$
//$$    public LibGuiBootstrap(FMLJavaModLoadingContext context) {
//$$        ModLoaderUtil.setModLoadingContext(context);
//$$            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> { 
//$$         new LibGuiClient().onInitializeClient();
//$$        });
//$$        new LibGuiCommon().onInitialize();
//$$    }
//$$    public LibGuiBootstrap() {
//$$        this(FMLJavaModLoadingContext.get());
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
//#if MC < 12109
//$$        if(FMLEnvironment.dist == Dist.CLIENT) {
//#else
//$$        if(FMLEnvironment.getDist() == Dist.CLIENT) {
//#endif
//$$         new LibGuiClient().onInitializeClient();
//$$        }
//$$        new LibGuiCommon().onInitialize();
//$$    }
//$$    
//$$}
//#endif
