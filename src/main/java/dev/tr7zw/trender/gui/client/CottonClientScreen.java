package dev.tr7zw.trender.gui.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
//#if MC >= 11800
import net.minecraft.client.gui.narration.NarrationElementOutput;
//#endif
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import dev.tr7zw.trender.gui.GuiDescription;
import dev.tr7zw.trender.gui.impl.VisualLogger;
import dev.tr7zw.trender.gui.impl.client.CottonScreenImpl;
//#if MC >= 11904
import dev.tr7zw.trender.gui.impl.client.FocusElements;
//#endif
import dev.tr7zw.trender.gui.impl.client.MouseInputHandler;
import dev.tr7zw.trender.gui.impl.client.NarrationHelper;
import dev.tr7zw.trender.gui.impl.mixin.client.ScreenAccessor;
import dev.tr7zw.trender.gui.widget.WPanel;
import dev.tr7zw.trender.gui.widget.WWidget;
import dev.tr7zw.trender.gui.widget.data.InputResult;
import dev.tr7zw.transition.mc.ComponentProvider;

//#if MC >= 12000
//#else
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//#endif

public class CottonClientScreen extends Screen implements CottonScreenImpl {
    private static final VisualLogger LOGGER = new VisualLogger(CottonInventoryScreen.class);
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

        //#if MC >= 11904
        if (root != null) {
            GuiEventListener rootPanelElement = FocusElements.ofPanel(root);
            ((ScreenAccessor) this).libgui$getChildren().add(rootPanelElement);
            setInitialFocus(rootPanelElement);
        } else {
            LOGGER.warn("No root panel found, keyboard navigation disabled");
        }
        //#endif
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
    //#if MC >= 12000
    public void render(GuiGraphics context, int mouseX, int mouseY, float partialTicks) {
        super.render(context, mouseX, mouseY, partialTicks);
        RenderContext renderContext = new RenderContext(context);
        //#else
        //$$public void render(PoseStack context, int mouseX, int mouseY, float partialTicks) {
        //$$    super.render(context, mouseX, mouseY, partialTicks);
        //$$    RenderContext renderContext = new RenderContext(this, context);
        //#endif
        //#if MC <= 11904
        //$$ super.renderBackground(renderContext.getPose());
        //#elseif MC <= 12001
        //$$ super.renderBackground(context);
        //#endif
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
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;
        mouseInputHandler.checkFocus(containerX, containerY);
        if (containerX < 0 || containerY < 0 || containerX >= width || containerY >= height)
            return true;
        mouseInputHandler.onMouseDown(containerX, containerY, mouseButton);

        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);

        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;
        mouseInputHandler.onMouseUp(containerX, containerY, mouseButton);

        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
        super.mouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);

        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;
        mouseInputHandler.onMouseDrag(containerX, containerY, mouseButton, deltaX, deltaY);

        return true;
    }

    @Override
    //#if MC >= 12002
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        //#else
        //$$ public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount) {
        //$$    super.mouseScrolled(mouseX, mouseY, horizontalAmount);
        //$$ double verticalAmount = 0;
        //#endif

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
    public boolean charTyped(char ch, int keyCode) {
        WWidget focus = description.getFocus();
        if (focus != null && focus.onCharTyped(ch) == InputResult.PROCESSED) {
            return true;
        }

        return super.charTyped(ch, keyCode);
    }

    @Override
    public boolean keyPressed(int ch, int keyCode, int modifiers) {
        WWidget focus = description.getFocus();
        if (focus != null && focus.onKeyPressed(ch, keyCode, modifiers) == InputResult.PROCESSED) {
            return true;
        }

        return super.keyPressed(ch, keyCode, modifiers);
    }

    @Override
    public boolean keyReleased(int ch, int keyCode, int modifiers) {
        WWidget focus = description.getFocus();
        if (focus != null && focus.onKeyReleased(ch, keyCode, modifiers) == InputResult.PROCESSED) {
            return true;
        }

        return super.keyReleased(ch, keyCode, modifiers);
    }

    //#if MC >= 11800
    @Override
    protected void updateNarratedWidget(NarrationElementOutput builder) {
        if (description != null)
            NarrationHelper.addNarrations(description.getRootPanel(), builder);
    }
    //#endif
}
