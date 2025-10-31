package dev.tr7zw.trender.gui.client;

import com.mojang.blaze3d.vertex.*;
import dev.tr7zw.transition.mc.*;
import dev.tr7zw.trender.gui.*;
import dev.tr7zw.trender.gui.impl.*;
import dev.tr7zw.trender.gui.impl.client.*;
import dev.tr7zw.trender.gui.impl.mixin.client.*;
import dev.tr7zw.trender.gui.widget.*;
import dev.tr7zw.trender.gui.widget.data.*;
import net.minecraft.client.gui.components.events.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.*;
import org.jetbrains.annotations.*;
import org.lwjgl.opengl.GL11;
//? if >= 1.18.0 {
import net.minecraft.client.gui.narration.*;
//? }

public class CottonClientScreen extends Screen implements CottonScreenImpl {
    private static final VisualLogger LOGGER = new VisualLogger(CottonClientScreen.class);
    protected GuiDescription description;
    protected int left = 0;
    protected int top = 0;

    /**
     * The X coordinate of the screen title. This is relative to the root panel's
     * top-left corner.
     *
     * @since 2.0.0
     */
    protected int titleX;

    /**
     * The Y coordinate of the screen title. This is relative to the root panel's
     * top-left corner.
     *
     * @since 2.0.0
     */
    protected int titleY;

    @Nullable
    protected WWidget lastResponder = null;

    private final MouseInputHandler<CottonClientScreen> mouseInputHandler = new MouseInputHandler<>(this);

    public CottonClientScreen(GuiDescription description) {
        this(ComponentProvider.EMPTY, description);
    }

    public CottonClientScreen(Component title, GuiDescription description) {
        super(title);
        this.description = description;
        description.getRootPanel().validate(description);
    }

    @Override
    public GuiDescription getDescription() {
        return description;
    }

    @Override
    public void init() {
        super.init();

        WPanel root = description.getRootPanel();
        if (root != null)
            root.addPainters();
        description.addPainters();
        reposition(width, height);

        //? if >= 1.19.4 {

        if (root != null) {
            GuiEventListener rootPanelElement = FocusElements.ofPanel(root);
            ((ScreenAccessor) this).libgui$getChildren().add(rootPanelElement);
            setInitialFocus(rootPanelElement);
        } else {
            LOGGER.warn("No root panel found, keyboard navigation disabled");
        }
        //? }
    }

    @Override
    public void removed() {
        super.removed();
        VisualLogger.reset();
    }

    @Nullable
    @Override
    public WWidget getLastResponder() {
        return lastResponder;
    }

    @Override
    public void setLastResponder(@Nullable WWidget lastResponder) {
        this.lastResponder = lastResponder;
    }

    /**
     * Repositions the root panel.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     */
    protected void reposition(int screenWidth, int screenHeight) {
        if (description != null) {
            WPanel root = description.getRootPanel();
            if (root != null) {
                titleX = description.getTitlePos().x();
                titleY = description.getTitlePos().y();

                if (!description.isFullscreen()) {
                    this.left = (screenWidth - root.getWidth()) / 2;
                    this.top = (screenHeight - root.getHeight()) / 2;
                } else {
                    this.left = 0;
                    this.top = 0;

                    root.setSize(screenWidth, screenHeight);
                }
            }
        }
    }

    private void paint(RenderContext context, int mouseX, int mouseY, float delta) {
        if (description != null) {
            WPanel root = description.getRootPanel();
            if (root != null) {
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                Scissors.refreshScissors();
                root.paint(context, left, top, mouseX - left, mouseY - top);
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                Scissors.checkStackIsEmpty();
            }

            if (getTitle() != null && description.isTitleVisible()) {
                int width = description.getRootPanel().getWidth();
                if (LibGui.getGuiStyle().isFontShadow()) {
                    ScreenDrawing.drawStringWithShadow(context, getTitle().getVisualOrderText(),
                            description.getTitleAlignment(), left + titleX, top + titleY, width - 2 * titleX,
                            description.getTitleColor());
                } else {
                    ScreenDrawing.drawString(context, getTitle().getVisualOrderText(), description.getTitleAlignment(),
                            left + titleX, top + titleY, width - 2 * titleX, description.getTitleColor());
                }
            }
        }
    }

    @Override
    //? if >= 1.20.0 {

    public void render(net.minecraft.client.gui.GuiGraphics context, int mouseX, int mouseY, float partialTicks) {
        super.render(context, mouseX, mouseY, partialTicks);
        RenderContext renderContext = new RenderContext(context);
        //? } else {
        /*
            public void render(PoseStack context, int mouseX, int mouseY, float partialTicks) {
        super.render(context, mouseX, mouseY, partialTicks);
        RenderContext renderContext = new RenderContext(this, context);
        *///? }
           //? if <= 1.19.4 {
           /*
           super.renderBackground(renderContext.getPose());
           *///? } else if <= 1.20.1 {
           /*
           super.renderBackground(context);
           *///? }
        paint(renderContext, mouseX, mouseY, partialTicks);

        if (description != null) {
            WPanel root = description.getRootPanel();
            if (root != null) {
                WWidget hitChild = root.hit(mouseX - left, mouseY - top);
                if (hitChild != null)
                    hitChild.renderTooltip(renderContext, left, top, mouseX - left, mouseY - top);
            }
        }

        VisualLogger.render(renderContext);
    }

    @Override
    public void tick() {
        super.tick();
        if (description != null) {
            WPanel root = description.getRootPanel();
            if (root != null) {
                root.tick();
            }
        }
    }

    @Override
    //? if >= 1.21.9 {

    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent mouseButtonEvent, boolean bl) {
        super.mouseClicked(mouseButtonEvent, bl);
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();
        int mouseButton = mouseButtonEvent.button();
        //? } else {
        /*
            public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        *///? }

        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;
        mouseInputHandler.checkFocus(containerX, containerY);
        if (containerX < 0 || containerY < 0 || containerX >= width || containerY >= height)
            return true;
        mouseInputHandler.onMouseDown(containerX, containerY, mouseButton);

        return true;
    }

    @Override
    //? if >= 1.21.9 {

    public boolean mouseReleased(net.minecraft.client.input.MouseButtonEvent mouseButtonEvent) {
        super.mouseReleased(mouseButtonEvent);
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();
        int mouseButton = mouseButtonEvent.button();
        //? } else {
        /*
            public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        *///? }

        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;
        mouseInputHandler.onMouseUp(containerX, containerY, mouseButton);

        return true;
    }

    @Override
    //? if >= 1.21.9 {

    public boolean mouseDragged(net.minecraft.client.input.MouseButtonEvent mouseButtonEvent, double deltaX,
            double deltaY) {
        super.mouseDragged(mouseButtonEvent, deltaX, deltaY);
        double mouseX = mouseButtonEvent.x();
        double mouseY = mouseButtonEvent.y();
        int mouseButton = mouseButtonEvent.button();
        //? } else {
        /*
            public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
        super.mouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
        *///? }

        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;
        mouseInputHandler.onMouseDrag(containerX, containerY, mouseButton, deltaX, deltaY);

        return true;
    }

    @Override
    //? if >= 1.20.2 {

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        //? } else {
        /*
            public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount) {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount);
        double verticalAmount = 0;
        *///? }

        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;
        mouseInputHandler.onMouseScroll(containerX, containerY, horizontalAmount, verticalAmount);

        return true;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);

        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;
        mouseInputHandler.onMouseMove(containerX, containerY);
    }

    @Override
    //? if >= 1.21.9 {

    public boolean charTyped(net.minecraft.client.input.CharacterEvent characterEvent) {
        char ch = characterEvent.codepointAsString().charAt(0);
        int keyCode = characterEvent.codepoint();
        //? } else {
        /*
            public boolean charTyped(char ch, int keyCode) {
        *///? }
        WWidget focus = description.getFocus();
        if (focus != null && focus.onCharTyped(ch) == InputResult.PROCESSED) {
            return true;
        }

        //? if >= 1.21.9 {

        return super.charTyped(characterEvent);
        //? } else {
        /*
        return super.charTyped(ch, keyCode);
        *///? }
    }

    @Override
    //? if >= 1.21.9 {

    public boolean keyPressed(net.minecraft.client.input.KeyEvent keyEvent) {
        char ch = (char) keyEvent.key();
        int keyCode = keyEvent.key();
        int modifiers = keyEvent.modifiers();
        //? } else {
        /*
            public boolean keyPressed(int ch, int keyCode, int modifiers) {
        *///? }
        WWidget focus = description.getFocus();
        if (focus != null && focus.onKeyPressed(ch, keyCode, modifiers) == InputResult.PROCESSED) {
            return true;
        }

        //? if >= 1.21.9 {

        return super.keyPressed(keyEvent);
        //? } else {
        /*
        return super.keyPressed(ch, keyCode, modifiers);
        *///? }
    }

    @Override
    //? if >= 1.21.9 {

    public boolean keyReleased(net.minecraft.client.input.KeyEvent keyEvent) {
        char ch = (char) keyEvent.key();
        int keyCode = keyEvent.key();
        int modifiers = keyEvent.modifiers();
        //? } else {
        /*
            public boolean keyReleased(int ch, int keyCode, int modifiers) {
        *///? }
        WWidget focus = description.getFocus();
        if (focus != null && focus.onKeyReleased(ch, keyCode, modifiers) == InputResult.PROCESSED) {
            return true;
        }

        //? if >= 1.21.9 {

        return super.keyReleased(keyEvent);
        //? } else {
        /*
        return super.keyReleased(ch, keyCode, modifiers);
        *///? }
    }

    //? if >= 1.18.0 {

    @Override
    protected void updateNarratedWidget(NarrationElementOutput builder) {
        if (description != null)
            NarrationHelper.addNarrations(description.getRootPanel(), builder);
    }
    //? }
}
