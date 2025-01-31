//#if FABRIC
package dev.tr7zw.trender.gui.impl.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import dev.tr7zw.trender.gui.client.CottonClientScreen;
import net.minecraft.network.chat.Component;

public class ModMenuSupport implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> new CottonClientScreen(Component.translatable("options.libgui.libgui_settings"),
                new ConfigGui(screen)) {
            @Override
            public void onClose() {
                this.minecraft.setScreen(screen);
            }
        };
    }
}
//#endif