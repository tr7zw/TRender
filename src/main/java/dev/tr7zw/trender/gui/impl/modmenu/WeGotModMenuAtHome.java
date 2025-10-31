package dev.tr7zw.trender.gui.impl.modmenu;

import dev.tr7zw.transition.*;
import dev.tr7zw.transition.mc.*;
import dev.tr7zw.trender.gui.client.*;
import dev.tr7zw.trender.gui.widget.*;
import dev.tr7zw.trender.gui.widget.data.*;
import java.util.function.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.*;

public class WeGotModMenuAtHome extends LightweightGuiDescription {

    public WeGotModMenuAtHome() {
        WGridPanel root = new WGridPanel(20);
        root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);

        var buttonList = new WListPanel<Function<Screen, Screen>, WButton>(
                ClientTRansitionMod.configScreenManager.getConfigScreens(), () -> new WButton(ComponentProvider.EMPTY),
                (s, l) -> {
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
