package dev.tr7zw.trender.gui.impl.client.constants;

import java.util.HashMap;
import java.util.Map;

import dev.tr7zw.trender.gui.impl.client.WidgetTextures;
import lombok.experimental.UtilityClass;
import net.minecraft.resources.ResourceLocation;

@UtilityClass
public class TextureConstants {

    public record SpriteData(int width, int height, int border) {
    }
    
    private static final Map<ResourceLocation, SpriteData> DATA = new HashMap<>();
    private static final SpriteData DEFAULT = new SpriteData(16, 16, 4);
    
    public static void register(ResourceLocation location, int width, int height, int border) {
        DATA.put(location, new SpriteData(width, height, border));
    }
    
    public static SpriteData get(ResourceLocation location) {
        return DATA.getOrDefault(location, DEFAULT);
    }
    
    static {
        register(WidgetTextures.getId("widget/panel_vanilla_old"), 32, 32, 2);
    }
    
}
