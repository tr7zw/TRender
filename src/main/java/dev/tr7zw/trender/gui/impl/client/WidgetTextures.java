package dev.tr7zw.trender.gui.impl.client;

import static dev.tr7zw.trender.gui.impl.LibGuiCommon.id;

import dev.tr7zw.util.NMSHelper;
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
    private static final WidgetSprites DARK_LABELED_SLIDER_HANDLE = new WidgetSprites(
            id(LEGACY_PATH + "widget/slider_handle_dark" + LEGACY_SUFFIX),
            id(LEGACY_PATH + "widget/slider_handle_highlighted_dark" + LEGACY_SUFFIX));
    private static final WidgetSprites DARK_BUTTON = new WidgetSprites(
            id(LEGACY_PATH + "widget/button_dark" + LEGACY_SUFFIX),
            id(LEGACY_PATH + "widget/button_disabled_dark" + LEGACY_SUFFIX),
            id(LEGACY_PATH + "widget/button_highlighted_dark" + LEGACY_SUFFIX));
    private static final ScrollBarTextures LIGHT_SCROLL_BAR = new ScrollBarTextures(
            id(LEGACY_PATH + "widget/scroll_bar/background_light" + LEGACY_SUFFIX),
            id(LEGACY_PATH + "widget/scroll_bar/thumb_light" + LEGACY_SUFFIX),
            id(LEGACY_PATH + "widget/scroll_bar/thumb_pressed_light" + LEGACY_SUFFIX),
            id(LEGACY_PATH + "widget/scroll_bar/thumb_hovered_light" + LEGACY_SUFFIX));
    private static final ScrollBarTextures DARK_SCROLL_BAR = new ScrollBarTextures(
            id(LEGACY_PATH + "widget/scroll_bar/background_dark" + LEGACY_SUFFIX),
            id(LEGACY_PATH + "widget/scroll_bar/thumb_dark" + LEGACY_SUFFIX),
            id(LEGACY_PATH + "widget/scroll_bar/thumb_pressed_dark" + LEGACY_SUFFIX),
            id(LEGACY_PATH + "widget/scroll_bar/thumb_hovered_dark" + LEGACY_SUFFIX));
    private static final WidgetSprites LIGHT_LABELED_SLIDER_HANDLE = new WidgetSprites(
            NMSHelper.getResourceLocation(NAMESPACE + ":" + LEGACY_PATH + "widget/slider_handle" + LEGACY_SUFFIX),
            NMSHelper.getResourceLocation(
                    NAMESPACE + ":" + LEGACY_PATH + "widget/slider_handle_highlighted" + LEGACY_SUFFIX));
    private static final WidgetSprites BUTTON_LIGH = new WidgetSprites(
            NMSHelper.getResourceLocation(NAMESPACE + ":" + LEGACY_PATH + "widget/button" + LEGACY_SUFFIX),
            NMSHelper.getResourceLocation(NAMESPACE + ":" + LEGACY_PATH + "widget/button_disabled" + LEGACY_SUFFIX),
            NMSHelper.getResourceLocation(NAMESPACE + ":" + LEGACY_PATH + "widget/button_highlighted" + LEGACY_SUFFIX));

    public static WidgetSprites getButtonTextures(boolean dark) {
        return dark ? DARK_BUTTON : BUTTON_LIGH;
    }

    public static WidgetSprites getLabeledSliderHandleTextures(boolean dark) {
        return dark ? DARK_LABELED_SLIDER_HANDLE : LIGHT_LABELED_SLIDER_HANDLE;
    }

    public static ScrollBarTextures getScrollBarTextures(boolean dark) {
        return dark ? DARK_SCROLL_BAR : LIGHT_SCROLL_BAR;
    }

    public record ScrollBarTextures(ResourceLocation background, ResourceLocation thumb, ResourceLocation thumbPressed,
            ResourceLocation thumbHovered) {
    }
}
