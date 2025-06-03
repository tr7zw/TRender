package dev.tr7zw.trender.gui.impl.modmenu;

import java.util.function.Function;

import dev.tr7zw.transition.ClientTRansitionMod;
import dev.tr7zw.transition.mc.ComponentProvider;
import dev.tr7zw.trender.gui.client.BackgroundPainter;
import dev.tr7zw.trender.gui.client.LightweightGuiDescription;
import dev.tr7zw.trender.gui.widget.WButton;
import dev.tr7zw.trender.gui.widget.WGridPanel;
import dev.tr7zw.trender.gui.widget.WListPanel;
import dev.tr7zw.trender.gui.widget.data.Insets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;

public class WeGotModMenuAtHome extends LightweightGuiDescription {

    public WeGotModMenuAtHome() {
        WGridPanel root = new WGridPanel(20);
        root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);
        
        System.out.println(ClientTRansitionMod.configScreenManager.getConfigScreens());
        var buttonList = new WListPanel<Function<Screen,Screen>, WButton>(
                ClientTRansitionMod.configScreenManager.getConfigScreens(), () -> new WButton(ComponentProvider.EMPTY), (s, l) -> {
                    l.setLabel(s.apply(null).getTitle());
                    l.setOnClick(() -> {
                        Minecraft.getInstance().setScreen(s.apply(Minecraft.getInstance().screen));
                    });
                });
        buttonList.setGap(2);
        buttonList.setSize(17 * 20, 8 * 20);
        root.add(buttonList, 0, 1, 10, 10);
        
        WButton doneButton = new WButton(CommonComponents.GUI_DONE);
        doneButton.setOnClick(() -> {
            Minecraft.getInstance().setScreen(null);
        });
        root.add(doneButton, 0, 12, 3, 1);

        root.setBackgroundPainter(BackgroundPainter.VANILLA);

        root.validate(this);
    }
    
}
