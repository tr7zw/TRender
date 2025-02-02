package dev.tr7zw.trender.gui.impl.client;

import dev.tr7zw.util.ComponentProvider;
import net.minecraft.network.chat.Component;

public final class NarrationMessages {
    public static final String ITEM_SLOT_TITLE_KEY = "widget.libgui.item_slot.narration.title";
    public static final String LABELED_SLIDER_TITLE_KEY = "widget.libgui.labeled_slider.narration.title";
    public static final Component SCROLL_BAR_TITLE = ComponentProvider
            .translatable("widget.libgui.scroll_bar.narration.title");
    public static final String SLIDER_MESSAGE_KEY = "widget.libgui.slider.narration.title";
    public static final Component SLIDER_USAGE = ComponentProvider.translatable("widget.libgui.slider.narration.usage");
    public static final String TAB_TITLE_KEY = "widget.libgui.tab.narration.title";
    public static final String TAB_POSITION_KEY = "widget.libgui.tab.narration.position";
    public static final String TEXT_FIELD_TITLE_KEY = "widget.libgui.text_field.narration.title";
    public static final String TEXT_FIELD_SUGGESTION_KEY = "widget.libgui.text_field.narration.suggestion";
    public static final String TOGGLE_BUTTON_NAMED_KEY = "widget.libgui.toggle_button.narration.named";
    public static final Component TOGGLE_BUTTON_OFF = ComponentProvider
            .translatable("widget.libgui.toggle_button.narration.off");
    public static final Component TOGGLE_BUTTON_ON = ComponentProvider
            .translatable("widget.libgui.toggle_button.narration.on");
    public static final String TOGGLE_BUTTON_UNNAMED_KEY = "widget.libgui.toggle_button.narration.unnamed";

    public static final class Vanilla {
        public static final Component BUTTON_USAGE_FOCUSED = ComponentProvider
                .translatable("narration.button.usage.focused");
        public static final Component BUTTON_USAGE_HOVERED = ComponentProvider
                .translatable("narration.button.usage.hovered");
        public static final Component COMPONENT_LIST_USAGE = ComponentProvider
                .translatable("narration.component_list.usage");
        public static final Component INVENTORY = ComponentProvider.translatable("container.inventory");
        public static final String SCREEN_POSITION_KEY = "narrator.position.screen";
        public static final Component HOTBAR = ComponentProvider.translatable("options.attack.hotbar");
    }
}
