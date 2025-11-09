//? if fabric {

package dev.tr7zw.trender.gui.impl.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import dev.tr7zw.trender.gui.client.CottonClientScreen;
import dev.tr7zw.transition.mc.ComponentProvider;

public class ModMenuSupport implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> new CottonClientScreen(ComponentProvider.translatable("text.trender.name"),
                new ConfigGui(screen)) {
            @Override
            public void onClose() {
                this.minecraft.setScreen(screen);
            }
        };
    }
}
//? }
