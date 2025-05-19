package dev.tr7zw.trender.gui.impl.client.style;

import java.util.EnumMap;
import java.util.Map;

import dev.tr7zw.trender.gui.client.LibGui;

public class TextureContainer<T> {

    private final T DEFAULT_LIGHT;
    private final T DEFAULT_DARK;
    private final Map<GuiStyle, T> values = new EnumMap<>(GuiStyle.class);
    
    public TextureContainer(T defaultLight, T defaultDark) {
        this.DEFAULT_LIGHT = defaultLight;
        this.DEFAULT_DARK = defaultDark;
    }
    
    public void register(GuiStyle style, T value) {
        values.put(style, value);
    }
    
    public T get(GuiStyle style) {
        return values.getOrDefault(style, style.isDark() ? DEFAULT_DARK : DEFAULT_LIGHT);
    }
    
    public T get() {
        return get(LibGui.getGuiStyle());
    }
    
}
