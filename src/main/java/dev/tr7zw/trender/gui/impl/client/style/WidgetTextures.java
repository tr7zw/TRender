package dev.tr7zw.trender.gui.impl.client.style;

import static dev.tr7zw.trender.gui.impl.LibGuiCommon.id;

import dev.tr7zw.transition.mc.GeneralUtil;
import dev.tr7zw.trender.gui.impl.LibGuiCommon;
import dev.tr7zw.trender.gui.impl.client.WidgetSprites;
import dev.tr7zw.trender.gui.widget.data.Texture;
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

    private static final WidgetSprites SLIDER = new WidgetSprites(getNamespacedId("widget/slider"),
            getNamespacedId("widget/slider_highlighted"));
    private static final ScrollBarTextures LIGHT_SCROLL_BAR = new ScrollBarTextures(
            getId("widget/scroll_bar/background_light"), getId("widget/scroll_bar/thumb_light"),
            getId("widget/scroll_bar/thumb_pressed_light"), getId("widget/scroll_bar/thumb_hovered_light"));
    private static final ScrollBarTextures DARK_SCROLL_BAR = new ScrollBarTextures(
            getId("widget/scroll_bar/background_dark"), getId("widget/scroll_bar/thumb_dark"),
            getId("widget/scroll_bar/thumb_pressed_dark"), getId("widget/scroll_bar/thumb_hovered_dark"));
    private static final WidgetSprites LIGHT_LABELED_SLIDER_HANDLE = new WidgetSprites(
            getNamespacedId("widget/slider_handle"), getNamespacedId("widget/slider_handle_highlighted"));
    private static final WidgetSprites DARK_LABELED_SLIDER_HANDLE = new WidgetSprites(
            getId("widget/slider_handle_dark"), getId("widget/slider_handle_highlighted_dark"));
    private static final WidgetSprites BUTTON_LIGH = new WidgetSprites(getNamespacedId("widget/button"),
            getNamespacedId("widget/button_disabled"), getNamespacedId("widget/button_highlighted"));
    private static final WidgetSprites DARK_BUTTON = new WidgetSprites(getId("widget/button_dark"),
            getId("widget/button_disabled_dark"), getId("widget/button_highlighted_dark"));
    private static final ResourceLocation LIGHT_TEXTURE = LibGuiCommon.id("textures/widget/slider_light.png");
    private static final ResourceLocation DARK_TEXTURE = LibGuiCommon.id("textures/widget/slider_dark.png");
    private static final ToggleButtonTextures TOGGLE_BUTTON_TEXTURES = new ToggleButtonTextures(
            new Texture(LibGuiCommon.id("textures/widget/toggle_on.png")), new Texture(LibGuiCommon.id("textures/widget/toggle_off.png")),
            new Texture(LibGuiCommon.id("textures/widget/toggle_focus.png")));
    private static final ToggleButtonTextures TOGGLE_BUTTON_VANILLA_TEXTURES = new ToggleButtonTextures(
            new Texture(LibGuiCommon.id("textures/widget/checkbox_selected.png")), new Texture(LibGuiCommon.id("textures/widget/checkbox.png")),
            new Texture(LibGuiCommon.id("textures/widget/checkbox_highlighted.png")));

    @Getter
    private static final TextureContainer<WidgetSprites> buttonTextures = new TextureContainer<>(
            BUTTON_LIGH, DARK_BUTTON);
    @Getter
    private static final TextureContainer<WidgetSprites> labeledSliderHandleTextures = new TextureContainer<>(
            LIGHT_LABELED_SLIDER_HANDLE, DARK_LABELED_SLIDER_HANDLE);
    @Getter
    private static final TextureContainer<ResourceLocation> sliderTextures = new TextureContainer<>(
            LIGHT_TEXTURE, DARK_TEXTURE);
    @Getter
    private static final TextureContainer<WidgetSprites> valueSliderTexture = new TextureContainer<>(
            SLIDER, SLIDER);
    @Getter
    private static final TextureContainer<ScrollBarTextures> scrollBarTextures = new TextureContainer<>(
            LIGHT_SCROLL_BAR, DARK_SCROLL_BAR);
    @Getter
    private static final TextureContainer<ToggleButtonTextures> toggleButtonTextures = new TextureContainer<>(TOGGLE_BUTTON_TEXTURES, TOGGLE_BUTTON_TEXTURES);
    
    static {
        toggleButtonTextures.register(GuiStyle.VANILLA_MODERN, TOGGLE_BUTTON_VANILLA_TEXTURES);
        toggleButtonTextures.register(GuiStyle.VANILLA_OLD, TOGGLE_BUTTON_VANILLA_TEXTURES);
    }
    
    public record ToggleButtonTextures(Texture on, Texture off, Texture focus) {
    }
    
    public record ScrollBarTextures(ResourceLocation background, ResourceLocation thumb, ResourceLocation thumbPressed,
            ResourceLocation thumbHovered) {
    }
}
