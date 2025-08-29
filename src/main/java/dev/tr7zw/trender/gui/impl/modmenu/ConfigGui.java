package dev.tr7zw.trender.gui.impl.modmenu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import dev.tr7zw.trender.gui.client.AbstractConfigScreen;
import dev.tr7zw.trender.gui.client.BackgroundPainter;
import dev.tr7zw.trender.gui.impl.client.LibGuiClient;
import dev.tr7zw.trender.gui.impl.client.config.LibGuiConfig;
import dev.tr7zw.trender.gui.impl.client.style.GuiStyle;
import dev.tr7zw.trender.gui.widget.WButton;
import dev.tr7zw.trender.gui.widget.WGridPanel;
import dev.tr7zw.trender.gui.widget.WLabeledDoubleSlider;
import dev.tr7zw.trender.gui.widget.WListPanel;
import dev.tr7zw.trender.gui.widget.WPlayerPreview;
import dev.tr7zw.trender.gui.widget.WTabPanel;
import dev.tr7zw.trender.gui.widget.WTextField;
import dev.tr7zw.trender.gui.widget.WToggleButton;
import dev.tr7zw.trender.gui.widget.data.Insets;
import dev.tr7zw.trender.gui.widget.icon.ItemIcon;
import dev.tr7zw.transition.mc.ComponentProvider;
import dev.tr7zw.transition.mc.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class ConfigGui extends AbstractConfigScreen {
    public ConfigGui(Screen previous) {
        super(ComponentProvider.literal("TRender"), previous);
        WGridPanel root = new WGridPanel(20);
        root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);

        WTabPanel wTabPanel = new WTabPanel();

        List<String> data = new ArrayList<String>(Arrays.asList("Some", "test", "data", "with", "many", "options", "so",
                "it", "needs", "to", "scroll.", "This", "menu", "is", "just", "for", "testing"));

        // options page
        List<OptionInstance> options = new ArrayList<>();
        options.add(getEnumOption("text.trender.style", GuiStyle.class, () -> LibGuiClient.config.style,
                (v) -> LibGuiClient.config.style = v));

        var optionList = createOptionList(options);
        optionList.setGap(-1);
        optionList.setSize(14 * 20, 9 * 20);

        wTabPanel.add(optionList,
                b -> b.title(ComponentProvider.literal("Settings")).icon(new ItemIcon(Items.COMMAND_BLOCK)));
        //        root.add(darkmodeButton, 0, 1, 6, 1);

        WLabeledDoubleSlider ds = new WLabeledDoubleSlider(0, 3, 0.2);
        ds.setLabelUpdater(v -> {
            DecimalFormat df = new DecimalFormat("###.##");
            return ComponentProvider.literal("Val: " + df.format(v));
        });

        //        root.add(ds, 4, 11, 3, 1);

        WListPanel<String, WToggleButton> testList = new WListPanel<String, WToggleButton>(data,
                () -> new WToggleButton(ComponentProvider.EMPTY), (s, l) -> {
                    l.setLabel(ComponentProvider.literal(s));
                    l.setToolip(ComponentProvider.literal("Tooltip for: " + s));
                });
        testList.setGap(0);
        testList.setSize(17 * 20, 8 * 20);

        wTabPanel.add(testList, b -> b.title(ComponentProvider.literal("list")).icon(new ItemIcon(Items.PAINTING)));
        WGridPanel test = new WGridPanel();
        test.setInsets(new Insets(2, 4));
        WToggleButton toggle = new WToggleButton(ComponentProvider.literal("I... am a toggle"));
        test.add(toggle, 0, 0);
        test.add(new WLabeledDoubleSlider(0, 1, 0.05, ComponentProvider.literal("Test Slider")), 0, 1, 5, 1);
        WPlayerPreview preview = new WPlayerPreview();
        preview.setShowBackground(true);
        test.add(preview, 5, 3);
        wTabPanel.add(test, b -> b.title(ComponentProvider.literal("Test Buttons")).icon(new ItemIcon(Items.APPLE)));
        //        wTabPanel.setSize(6, 6);

        wTabPanel.layout();
        root.add(wTabPanel, 0, 1);

        List<Entry<ResourceKey<Item>, Item>> items = new ArrayList<>(ItemUtil.getItems());
        items.sort((a, b) -> a.getKey().location().toString().compareTo(b.getKey().location().toString()));

        WListPanel<Entry<ResourceKey<Item>, Item>, WToggleButton> itemList = new WListPanel<Entry<ResourceKey<Item>, Item>, WToggleButton>(
                items, () -> new WToggleButton(ComponentProvider.EMPTY), (s, l) -> {
                    l.setLabel(s.getValue().getName(s.getValue().getDefaultInstance()));
                    l.setToolip(ComponentProvider.literal(s.getKey().location().toString()));
                    l.setIcon(new ItemIcon(s.getValue()));
                });
        itemList.setGap(-1);
        itemList.setInsets(new Insets(2, 4));
        //itemList.setSize(17*20, 8*20);
        WGridPanel itemTab = new WGridPanel(20);
        itemTab.add(itemList, 0, 0, 17, 7);
        WTextField searchField = new WTextField();
        searchField.setChangedListener(s -> {
            itemList.setFilter(e -> e.getKey().location().toString().toLowerCase().contains(s.toLowerCase()));
            itemList.layout();
        });
        itemTab.add(searchField, 0, 7, 17, 1);
        wTabPanel.add(itemTab, b -> b.title(ComponentProvider.literal("Items")).icon(new ItemIcon(Items.GRASS_BLOCK)));

        // Scroll test
        wTabPanel.add(test, b -> b.title(ComponentProvider.literal("Test Buttons2")).icon(new ItemIcon(Items.ARROW)));
        wTabPanel.add(test, b -> b.title(ComponentProvider.literal("Test Buttons3")).icon(new ItemIcon(Items.BELL)));

        var kirb = new WKirbSprite();
        root.add(kirb, 16, 10);
        kirb.setToolip(ComponentProvider.literal("Kirby"));

        WButton doneButton = new WButton(CommonComponents.GUI_DONE);
        doneButton.setOnClick(() -> {
            Minecraft.getInstance().setScreen(previous);
        });
        root.add(doneButton, 0, 11, 3, 1);

        root.setBackgroundPainter(BackgroundPainter.VANILLA);

        root.validate(this);
    }

    @Override
    public void save() {
        LibGuiClient.saveConfig(LibGuiClient.config);
    }

    @Override
    public void reset() {
        LibGuiClient.config = new LibGuiConfig();
    }
}
