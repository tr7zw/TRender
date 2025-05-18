package dev.tr7zw.trender.gui.impl.client;

import static dev.tr7zw.trender.gui.impl.LibGuiCommon.id;

import dev.tr7zw.transition.mc.GeneralUtil;
import dev.tr7zw.trender.gui.client.LibGui;
import dev.tr7zw.trender.gui.impl.LibGuiCommon;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

public final class WidgetTextures {
    //#if MC >= 12002
    private static final String NAMESPACE = "minecraft";
    private static final String LEGACY_PATH = "";
    private static final String LEGACY_SUFFIX = "";
    //#else
    //$$ private static final String NAMESPACE = "trender";
    //$$ private static final String LEGACY_PATH = "textures/gui/sprites/";
    //$$ private static final String LEGACY_SUFFIX = ".png";
    //#endif

    public static ResourceLocation getId(String path) {
        return id(LEGACY_PATH + path + LEGACY_SUFFIX);
    }

    public static ResourceLocation getNamespacedId(String path) {
        return GeneralUtil.getResourceLocation(NAMESPACE + ":" + LEGACY_PATH + path + LEGACY_SUFFIX);
    }

    @Getter
    private static final WidgetSprites SLIDER = new WidgetSprites(getNamespacedId("widget/slider"),
            getNamespacedId("widget/slider_highlighted"));
    private static final WidgetSprites DARK_LABELED_SLIDER_HANDLE = new WidgetSprites(
            getId("widget/slider_handle_dark"), getId("widget/slider_handle_highlighted_dark"));
    private static final WidgetSprites DARK_BUTTON = new WidgetSprites(getId("widget/button_dark"),
            getId("widget/button_disabled_dark"), getId("widget/button_highlighted_dark"));
    private static final ScrollBarTextures LIGHT_SCROLL_BAR = new ScrollBarTextures(
            getId("widget/scroll_bar/background_light"), getId("widget/scroll_bar/thumb_light"),
            getId("widget/scroll_bar/thumb_pressed_light"), getId("widget/scroll_bar/thumb_hovered_light"));
    private static final ScrollBarTextures DARK_SCROLL_BAR = new ScrollBarTextures(
            getId("widget/scroll_bar/background_dark"), getId("widget/scroll_bar/thumb_dark"),
            getId("widget/scroll_bar/thumb_pressed_dark"), getId("widget/scroll_bar/thumb_hovered_dark"));
    private static final WidgetSprites LIGHT_LABELED_SLIDER_HANDLE = new WidgetSprites(
            getNamespacedId("widget/slider_handle"), getNamespacedId("widget/slider_handle_highlighted"));
    private static final WidgetSprites BUTTON_LIGH = new WidgetSprites(getNamespacedId("widget/button"),
            getNamespacedId("widget/button_disabled"), getNamespacedId("widget/button_highlighted"));
    public static final ResourceLocation LIGHT_TEXTURE = LibGuiCommon.id("textures/widget/slider_light.png");
    public static final ResourceLocation DARK_TEXTURE = LibGuiCommon.id("textures/widget/slider_dark.png");
    
    
    public static WidgetSprites getButtonTextures() {
        return LibGui.getGuiStyle().isDark() ? DARK_BUTTON : BUTTON_LIGH;//DARK_BUTTON : BUTTON_LIGH;
    }

    public static WidgetSprites getLabeledSliderHandleTextures() {
        return LibGui.getGuiStyle().isDark() ? DARK_LABELED_SLIDER_HANDLE :  LIGHT_LABELED_SLIDER_HANDLE;//dark ? DARK_LABELED_SLIDER_HANDLE : LIGHT_LABELED_SLIDER_HANDLE;
    }

    public static ScrollBarTextures getScrollBarTextures() {
        return LibGui.getGuiStyle().isDark() ? DARK_SCROLL_BAR : LIGHT_SCROLL_BAR;//dark ? DARK_SCROLL_BAR : LIGHT_SCROLL_BAR;
    }
    
    public static ResourceLocation getSliderTextures() {
        return LibGui.getGuiStyle().isDark() ? DARK_TEXTURE : LIGHT_TEXTURE;
    }

    public record ScrollBarTextures(ResourceLocation background, ResourceLocation thumb, ResourceLocation thumbPressed,
            ResourceLocation thumbHovered) {
    }
}
