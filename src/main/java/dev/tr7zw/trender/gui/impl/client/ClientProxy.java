package dev.tr7zw.trender.gui.impl.client;

import dev.tr7zw.trender.gui.impl.Proxy;
import dev.tr7zw.trender.gui.widget.WWidget;

public final class ClientProxy extends Proxy {
    @Override
    public void addPainters(WWidget widget) {
        widget.addPainters();
    }
}
