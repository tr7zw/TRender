package dev.tr7zw.trender.gui.impl.client.style;

import java.util.HashMap;
import java.util.Map;

import lombok.experimental.UtilityClass;
import net.minecraft.resources.*;

@UtilityClass
public class TextureConstants {

    public record SpriteData(int width, int height, int border) {
    }

    private static final Map<Identifier, SpriteData> DATA = new HashMap<>();
    private static final SpriteData DEFAULT = new SpriteData(16, 16, 4);

    public static void register(Identifier location, int width, int height, int border) {
        DATA.put(location, new SpriteData(width, height, border));
    }

    public static SpriteData get(Identifier location) {
        return DATA.getOrDefault(location, DEFAULT);
    }

    static {
        register(WidgetTextures.getId("widget/panel_vanilla_old"), 32, 32, 2);
    }

}
