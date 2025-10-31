package dev.tr7zw.trender.gui.impl.client;

import dev.tr7zw.trender.gui.*;
import dev.tr7zw.trender.gui.widget.*;
import org.jetbrains.annotations.*;

public interface CottonScreenImpl {
    GuiDescription getDescription();

    @Nullable
    WWidget getLastResponder();

    void setLastResponder(@Nullable WWidget lastResponder);
}
