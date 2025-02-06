package dev.tr7zw.trender.gui.impl.modmenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.tr7zw.trender.gui.client.BackgroundPainter;
import dev.tr7zw.trender.gui.client.LightweightGuiDescription;
import dev.tr7zw.trender.gui.impl.client.LibGuiClient;
import dev.tr7zw.trender.gui.widget.TooltipBuilder;
import dev.tr7zw.trender.gui.widget.WButton;
import dev.tr7zw.trender.gui.widget.WGridPanel;
import dev.tr7zw.trender.gui.widget.WLabel;
import dev.tr7zw.trender.gui.widget.WListPanel;
import dev.tr7zw.trender.gui.widget.WTabPanel;
import dev.tr7zw.trender.gui.widget.WToggleButton;
import dev.tr7zw.trender.gui.widget.data.Insets;
import dev.tr7zw.trender.gui.widget.icon.ItemIcon;
import dev.tr7zw.util.ComponentProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

public class ConfigGui extends LightweightGuiDescription {
    public ConfigGui(Screen previous) {
        WGridPanel root = new WGridPanel(20);
        root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);

        List<String> data = new ArrayList<String>(Arrays.asList("Some", "test", "data", "with", "many", "options", "so", "it", "needs", "to", "scroll"));
        
        WToggleButton darkmodeButton = new WToggleButton(ComponentProvider.translatable("option.libgui.darkmode")) {
            @Override
            public void onToggle(boolean on) {
                LibGuiClient.config.darkMode = on;
                LibGuiClient.saveConfig(LibGuiClient.config);
                data.add(on ? "On" + System.currentTimeMillis() : "Off" + System.currentTimeMillis());
            }
        };
        darkmodeButton.setToggle(LibGuiClient.config.darkMode);
//        root.add(darkmodeButton, 0, 1, 6, 1);

        WListPanel<String, WToggleButton> testList = new WListPanel<String, WToggleButton>(data,
                () -> new WToggleButton(ComponentProvider.EMPTY), (s,l) -> {
                l.setLabel(ComponentProvider.literal(s));
                l.setToolip(ComponentProvider.literal("Tooltip for: " + s));
                });
        testList.setGap(0);
        testList.setSize(18*20, 8*20);
        WTabPanel wTabPanel = new WTabPanel();
        wTabPanel.add(testList, b -> b.title(ComponentProvider.literal("list")).icon(new ItemIcon(Items.PAINTING)));
        wTabPanel.add(darkmodeButton, b -> b.title(ComponentProvider.literal("Darkmode")).icon(new ItemIcon(Items.APPLE)));
//        wTabPanel.setSize(6, 6);
        wTabPanel.layout();
        root.add(wTabPanel, 0, 1);
        
//      root.add(testList, 0, 2, 6, 6);

        var kirb = new WKirbSprite();
        root.add(kirb, 17, 10);
        kirb.setToolip(ComponentProvider.literal("Kirby"));

        WButton doneButton = new WButton(CommonComponents.GUI_DONE);
        doneButton.setOnClick(() -> {
            Minecraft.getInstance().setScreen(previous);
        });
        root.add(doneButton, 0, 11, 3, 1);

        root.setBackgroundPainter(BackgroundPainter.VANILLA);

        root.validate(this);
        System.out.println(wTabPanel.getWidth() + " " + wTabPanel.getHeight());
    }
}
