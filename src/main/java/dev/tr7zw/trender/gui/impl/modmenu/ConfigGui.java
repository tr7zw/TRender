package dev.tr7zw.trender.gui.impl.modmenu;

import dev.tr7zw.transition.*;
import dev.tr7zw.transition.mc.*;
import dev.tr7zw.trender.gui.client.*;
import dev.tr7zw.trender.gui.impl.client.*;
import dev.tr7zw.trender.gui.impl.client.style.*;
import dev.tr7zw.trender.gui.widget.*;
import dev.tr7zw.trender.gui.widget.data.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.*;

import java.util.*;

public class ConfigGui extends AbstractConfigScreen {
    public ConfigGui(Screen previous) {
        super(ComponentProvider.translatable("text.trender.name"), previous);

        WGridPanel root = new WGridPanel(8);
        root.setBackgroundPainter(BackgroundPainter.VANILLA);
        root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);

        WTabPanel wTabPanel = new WTabPanel();

        var inst = ClientTRansitionMod.config;
        List<OptionInstance> generalOptions = new ArrayList<>();
        generalOptions.add(getEnumOption("text.trender.style", GuiStyle.class, () -> LibGuiClient.config.getConfig().style,
                (v) -> LibGuiClient.config.getConfig().style = v));
        generalOptions.add(getOnOffOption("text.trender.sendstacktraces", () -> inst.getConfig().userConsentedToSendCrashReports,
                b -> inst.getConfig().userConsentedToSendCrashReports = b));


        var generalOptionList = createOptionList(generalOptions);
        generalOptionList.setGap(-1);
        generalOptionList.setSize(14 * 20, 9 * 20);
        wTabPanel.add(generalOptionList,
                b -> b.title(ComponentProvider.translatable("text.trender.tab.general_options")));

        root.add(wTabPanel, 0, 1);

        WButton doneButton = new WButton(CommonComponents.GUI_DONE);
        doneButton.setOnClick(() -> {
            save();
            Minecraft.getInstance().setScreen(previous);
        });
        root.add(doneButton, 0, 27, 6, 2);

        WButton resetButton = new WButton(ComponentProvider.translatable("controls.reset"));
        resetButton.setOnClick(() -> {
            reset();
            root.layout();
        });
        root.add(resetButton, 29, 27, 6, 2);

        root.validate(this);
        root.setHost(this);
    }

    @Override
    public void save() {
        LibGuiClient.config.writeConfig();
        ClientTRansitionMod.config.writeConfig();
    }

    @Override
    public void reset() {
        LibGuiClient.config.reset();
        ClientTRansitionMod.config.reset();
    }
}
