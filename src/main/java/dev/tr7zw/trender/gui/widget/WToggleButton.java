package dev.tr7zw.trender.gui.widget;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import dev.tr7zw.trender.gui.client.RenderContext;
import dev.tr7zw.trender.gui.client.ScreenDrawing;
import dev.tr7zw.trender.gui.impl.LibGuiCommon;
import dev.tr7zw.trender.gui.impl.client.NarrationMessages;
import dev.tr7zw.trender.gui.widget.data.InputResult;
import dev.tr7zw.trender.gui.widget.data.Texture;
import dev.tr7zw.trender.gui.widget.icon.Icon;
import dev.tr7zw.transition.mc.ComponentProvider;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
//#if MC >= 11800
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
//#endif
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class WToggleButton extends WWidget {
    private static final int ICON_SIZE = 16;
    // Default on/off images
    protected static final Texture DEFAULT_OFF_IMAGE = new Texture(LibGuiCommon.id("textures/widget/toggle_off.png"));
    protected static final Texture DEFAULT_ON_IMAGE = new Texture(LibGuiCommon.id("textures/widget/toggle_on.png"));
    protected static final Texture DEFAULT_FOCUS_IMAGE = new Texture(
            LibGuiCommon.id("textures/widget/toggle_focus.png"));

    protected Texture onImage;
    protected Texture offImage;
    protected Texture focusImage = DEFAULT_FOCUS_IMAGE;

    @Nullable
    protected Component label = null;
    @Nullable
    @Getter
    @Setter
    private Icon icon;

    protected boolean isOn = false;
    @Nullable
    protected Consumer<Boolean> onToggle = null;

    protected int color = WLabel.DEFAULT_TEXT_COLOR;
    protected int darkmodeColor = WLabel.DEFAULT_DARKMODE_TEXT_COLOR;

    /**
     * Constructs a toggle button with default images and no label.
     */
    public WToggleButton() {
        this(DEFAULT_ON_IMAGE, DEFAULT_OFF_IMAGE);
    }

    /**
     * Constructs a toggle button with default images.
     *
     * @param label the button label
     */
    public WToggleButton(Component label) {
        this(DEFAULT_ON_IMAGE, DEFAULT_OFF_IMAGE);
        this.label = label;
    }

    /**
     * Constructs a toggle button with custom images and no label.
     *
     * @param onImage  the toggled on image
     * @param offImage the toggled off image
     */
    public WToggleButton(ResourceLocation onImage, ResourceLocation offImage) {
        this(new Texture(onImage), new Texture(offImage));
    }

    /**
     * Constructs a toggle button with custom images.
     *
     * @param onImage  the toggled on image
     * @param offImage the toggled off image
     * @param label    the button label
     */
    public WToggleButton(ResourceLocation onImage, ResourceLocation offImage, Component label) {
        this(new Texture(onImage), new Texture(offImage), label);
    }

    /**
     * Constructs a toggle button with custom images and no label.
     *
     * @param onImage  the toggled on image
     * @param offImage the toggled off image
     * @since 3.0.0
     */
    public WToggleButton(Texture onImage, Texture offImage) {
        this.onImage = onImage;
        this.offImage = offImage;
    }

    /**
     * Constructs a toggle button with custom images.
     *
     * @param onImage  the toggled on image
     * @param offImage the toggled off image
     * @param label    the button label
     * @since 3.0.0
     */
    public WToggleButton(Texture onImage, Texture offImage, Component label) {
        this.onImage = onImage;
        this.offImage = offImage;
        this.label = label;
    }

    @Override
    public void paint(RenderContext context, int x, int y, int mouseX, int mouseY) {
        ScreenDrawing.texturedRect(context, x, y, 18, 18, isOn ? onImage : offImage, 0xFFFFFFFF);
        if (isFocused()) {
            ScreenDrawing.texturedRect(context, x, y, 18, 18, focusImage, 0xFFFFFFFF);
        }
        int xPos = x + 22;
        if (icon != null) {
            icon.paint(context, x + 22, y + 1, ICON_SIZE);
            xPos += ICON_SIZE + 2;
        }
        if (label != null) {
            ScreenDrawing.drawString(context, label.getVisualOrderText(), xPos, y + 6,
                    shouldRenderInDarkMode() ? darkmodeColor : color);
        }
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

        this.isOn = !this.isOn;
        onToggle(this.isOn);
        return InputResult.PROCESSED;
    }

    @Override
    public InputResult onKeyPressed(int ch, int key, int modifiers) {
        if (isActivationKey(ch)) {
            onClick(0, 0, 0);
            return InputResult.PROCESSED;
        }

        return InputResult.IGNORED;
    }

    protected void onToggle(boolean on) {
        if (this.onToggle != null) {
            this.onToggle.accept(on);
        }
    }

    public boolean getToggle() {
        return this.isOn;
    }

    public void setToggle(boolean on) {
        this.isOn = on;
    }

    @Nullable
    public Consumer<Boolean> getOnToggle() {
        return this.onToggle;
    }

    public WToggleButton setOnToggle(@Nullable Consumer<Boolean> onToggle) {
        this.onToggle = onToggle;
        return this;
    }

    @Nullable
    public Component getLabel() {
        return label;
    }

    public WToggleButton setLabel(@Nullable Component label) {
        this.label = label;
        return this;
    }

    public WToggleButton setColor(int light, int dark) {
        this.color = light;
        this.darkmodeColor = dark;

        return this;
    }

    public Texture getOnImage() {
        return onImage;
    }

    public WToggleButton setOnImage(Texture onImage) {
        this.onImage = onImage;
        return this;
    }

    public Texture getOffImage() {
        return offImage;
    }

    public WToggleButton setOffImage(Texture offImage) {
        this.offImage = offImage;
        return this;
    }

    public Texture getFocusImage() {
        return focusImage;
    }

    public WToggleButton setFocusImage(Texture focusImage) {
        this.focusImage = focusImage;
        return this;
    }

    //#if MC >= 11800
    @Override
    public void addNarrations(NarrationElementOutput builder) {
        Component onOff = isOn ? NarrationMessages.TOGGLE_BUTTON_ON : NarrationMessages.TOGGLE_BUTTON_OFF;
        Component title;

        if (label != null) {
            title = ComponentProvider.translatable(NarrationMessages.TOGGLE_BUTTON_NAMED_KEY, label, onOff);
        } else {
            title = ComponentProvider.translatable(NarrationMessages.TOGGLE_BUTTON_UNNAMED_KEY, onOff);
        }

        builder.add(NarratedElementType.TITLE, title);

        if (isFocused()) {
            builder.add(NarratedElementType.USAGE, NarrationMessages.Vanilla.BUTTON_USAGE_FOCUSED);
        } else if (isHovered()) {
            builder.add(NarratedElementType.USAGE, NarrationMessages.Vanilla.BUTTON_USAGE_HOVERED);
        }
    }
    //#endif
}
