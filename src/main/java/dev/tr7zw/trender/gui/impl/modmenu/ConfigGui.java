package dev.tr7zw.trender.gui.impl.modmenu;

import dev.tr7zw.trender.gui.client.BackgroundPainter;
import dev.tr7zw.trender.gui.client.LightweightGuiDescription;
import dev.tr7zw.trender.gui.impl.client.LibGuiClient;
import dev.tr7zw.trender.gui.widget.WButton;
import dev.tr7zw.trender.gui.widget.WGridPanel;
import dev.tr7zw.trender.gui.widget.WToggleButton;
import dev.tr7zw.trender.gui.widget.data.Insets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ConfigGui extends LightweightGuiDescription {
    public ConfigGui(Screen previous) {
        WGridPanel root = new WGridPanel(20);
        root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);

        WToggleButton darkmodeButton = new WToggleButton(Component.translatable("option.libgui.darkmode")) {
            @Override
            public void onToggle(boolean on) {
                LibGuiClient.config.darkMode = on;
                LibGuiClient.saveConfig(LibGuiClient.config);
            }
        };
        darkmodeButton.setToggle(LibGuiClient.config.darkMode);
        root.add(darkmodeButton, 0, 1, 6, 1);

        root.add(new WKirbSprite(), 5, 2);

        WButton doneButton = new WButton(CommonComponents.GUI_DONE);
        doneButton.setOnClick(() -> {
            Minecraft.getInstance().setScreen(previous);
        });
        root.add(doneButton, 0, 3, 3, 1);

        root.setBackgroundPainter(BackgroundPainter.VANILLA);

        root.validate(this);
    }
}
