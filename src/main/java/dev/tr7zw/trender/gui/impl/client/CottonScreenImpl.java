package dev.tr7zw.trender.gui.impl.client;

import org.jetbrains.annotations.Nullable;

import dev.tr7zw.trender.gui.GuiDescription;
import dev.tr7zw.trender.gui.widget.WWidget;

public interface CottonScreenImpl {
    GuiDescription getDescription();

    @Nullable
    WWidget getLastResponder();

    void setLastResponder(@Nullable WWidget lastResponder);
}
