package dev.tr7zw.trender.gui.widget.focus;

import dev.tr7zw.trender.gui.widget.data.*;
import org.jetbrains.annotations.*;

/**
 * A focus is a focusable area in a widget.
 *
 * <p>
 * Foci can also carry a "key", which is a custom data value used to identify a
 * specific focus. For example, an item slot grid widget might use
 * {@code K = Integer} to identify each individual slot.
 *
 * @param key  the key
 * @param area the focusable area in widget space
 * @param <K>  the key type
 * @since 7.0.0
 */
public record Focus<K>(K key, Rect2i area) {
    /**
     * Creates a focus of an area and {@code null} data.
     *
     * @param area the area
     * @return the focus
     */
    public static Focus<@Nullable Void> of(Rect2i area) {
        return new Focus<>(null, area);
    }
}
