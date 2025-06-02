package dev.tr7zw.trender.gui.client;

import net.minecraft.world.inventory.ContainerData;
import org.jetbrains.annotations.Nullable;

import dev.tr7zw.trender.gui.GuiDescription;
import dev.tr7zw.trender.gui.ValidatedSlot;
import dev.tr7zw.trender.gui.widget.WGridPanel;
import dev.tr7zw.trender.gui.widget.WPanel;
import dev.tr7zw.trender.gui.widget.WWidget;
import dev.tr7zw.trender.gui.widget.data.HorizontalAlignment;
import dev.tr7zw.trender.gui.widget.data.Insets;
import dev.tr7zw.trender.gui.widget.data.Vec2i;

/**
 * A GuiDescription without any associated Minecraft classes
 */
public class LightweightGuiDescription implements GuiDescription {
    protected WPanel rootPanel = new WGridPanel().setInsets(Insets.ROOT_PANEL);
    protected ContainerData propertyDelegate;
    protected WWidget focus;

    protected Integer titleColor = null;
    protected boolean fullscreen = false;
    protected boolean titleVisible = true;
    protected HorizontalAlignment titleAlignment = HorizontalAlignment.LEFT;
    private Vec2i titlePos = new Vec2i(8, 6);
    private boolean useDefaultRootBackground = true;

    @Override
    public WPanel getRootPanel() {
        return rootPanel;
    }

    @Override
    public int getTitleColor() {
        return titleColor != null ? titleColor : getStyle().getTitleColor();
    }

    @Override
    public GuiDescription setRootPanel(WPanel panel) {
        this.rootPanel = panel;
        return this;
    }

    @Override
    public GuiDescription setTitleColor(int color) {
        this.titleColor = color;
        return this;
    }

    @Override
    public void addPainters() {
        if (this.rootPanel != null && !fullscreen && getUseDefaultRootBackground()) {
            this.rootPanel.setBackgroundPainter(BackgroundPainter.VANILLA);
        }
    }

    @Override
    public boolean getUseDefaultRootBackground() {
        return useDefaultRootBackground;
    }

    @Override
    public void setUseDefaultRootBackground(boolean useDefaultRootBackground) {
        this.useDefaultRootBackground = useDefaultRootBackground;
    }

    @Override
    public void addSlotPeer(ValidatedSlot slot) {
        //NO-OP
    }

    @Override
    @Nullable
    public ContainerData getPropertyDelegate() {
        return propertyDelegate;
    }

    @Override
    public GuiDescription setPropertyDelegate(ContainerData delegate) {
        this.propertyDelegate = delegate;
        return this;
    }

    @Override
    public boolean isFocused(WWidget widget) {
        return widget == focus;
    }

    @Override
    public WWidget getFocus() {
        return focus;
    }

    @Override
    public void requestFocus(WWidget widget) {
        //TODO: Are there circumstances where focus can't be stolen?
        if (focus == widget)
            return; //Nothing happens if we're already focused
        if (!widget.canFocus())
            return; //This is kind of a gotcha but needs to happen
        if (focus != null)
            focus.onFocusLost();
        focus = widget;
        focus.onFocusGained();
    }

    @Override
    public void releaseFocus(WWidget widget) {
        if (focus == widget) {
            focus = null;
            widget.onFocusLost();
        }
    }

    @Override
    public boolean isFullscreen() {
        return fullscreen;
    }

    @Override
    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    @Override
    public boolean isTitleVisible() {
        return titleVisible;
    }

    @Override
    public void setTitleVisible(boolean titleVisible) {
        this.titleVisible = titleVisible;
    }

    @Override
    public HorizontalAlignment getTitleAlignment() {
        return titleAlignment;
    }

    @Override
    public void setTitleAlignment(HorizontalAlignment titleAlignment) {
        this.titleAlignment = titleAlignment;
    }

    @Override
    public Vec2i getTitlePos() {
        return titlePos;
    }

    @Override
    public void setTitlePos(Vec2i titlePos) {
        this.titlePos = titlePos;
    }
}
