package dev.tr7zw.trender.gui.widget;

import dev.tr7zw.transition.mc.*;
import dev.tr7zw.trender.gui.client.*;
import dev.tr7zw.trender.gui.impl.client.*;
import dev.tr7zw.trender.gui.impl.client.style.*;
import dev.tr7zw.trender.gui.widget.data.*;
import dev.tr7zw.trender.gui.widget.icon.*;
import java.util.function.*;
import lombok.*;
import net.minecraft.client.*;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.*;
import org.jetbrains.annotations.*;
//? if >= 1.18.0 {
import net.minecraft.client.gui.narration.*;
//? }

public class WToggleButton extends WWidget {
    private static final int ICON_SIZE = 16;

    protected Texture onImage;
    protected Texture offImage;
    protected Texture focusImage;

    @Nullable
    protected Component label = null;
    @Nullable
    @Getter
    @Setter
    private Icon icon;

    protected boolean isOn = false;
    @Nullable
    protected Consumer<Boolean> onToggle = null;

    /**
     * Constructs a toggle button with default images and no label.
     */
    public WToggleButton() {

    }

    /**
     * Constructs a toggle button with default images.
     *
     * @param label the button label
     */
    public WToggleButton(Component label) {
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
        ScreenDrawing.texturedRect(context, x, y, 18, 18, isOn ? getOnImage() : getOffImage(), 0xFFFFFFFF, 18, 18);
        if (isFocused()) {
            ScreenDrawing.texturedRect(context, x, y, 18, 18, getFocusImage(), 0xFFFFFFFF, 18, 18);
        }
        int xPos = x + 22;
        if (icon != null) {
            icon.paint(context, x + 22, y + 1, ICON_SIZE);
            xPos += ICON_SIZE + 2;
        }
        if (label != null) {
            context.drawString(Minecraft.getInstance().font, label.getVisualOrderText(), xPos, y + 6,
                    LibGui.getGuiStyle().getTitleColor(), false);
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

    public Texture getOnImage() {
        return onImage != null ? onImage : WidgetTextures.getToggleButtonTextures().get().on();
    }

    public WToggleButton setOnImage(Texture onImage) {
        this.onImage = onImage;
        return this;
    }

    public Texture getOffImage() {
        return offImage != null ? offImage : WidgetTextures.getToggleButtonTextures().get().off();
    }

    public WToggleButton setOffImage(Texture offImage) {
        this.offImage = offImage;
        return this;
    }

    public Texture getFocusImage() {
        return focusImage != null ? focusImage : WidgetTextures.getToggleButtonTextures().get().focus();
    }

    public WToggleButton setFocusImage(Texture focusImage) {
        this.focusImage = focusImage;
        return this;
    }

    //? if >= 1.18.0 {

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
    //? }
}
