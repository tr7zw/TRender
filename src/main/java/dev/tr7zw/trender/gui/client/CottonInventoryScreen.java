package dev.tr7zw.trender.gui.client;

import com.mojang.blaze3d.platform.Lighting;

import dev.tr7zw.trender.gui.GuiDescription;
import dev.tr7zw.trender.gui.SyncedGuiDescription;
import dev.tr7zw.trender.gui.impl.VisualLogger;
import dev.tr7zw.trender.gui.impl.client.CottonScreenImpl;
import dev.tr7zw.trender.gui.impl.client.FocusElements;
import dev.tr7zw.trender.gui.impl.client.MouseInputHandler;
import dev.tr7zw.trender.gui.impl.client.NarrationHelper;
import dev.tr7zw.trender.gui.impl.mixin.client.ScreenAccessor;
import dev.tr7zw.trender.gui.widget.WPanel;
import dev.tr7zw.trender.gui.widget.WWidget;
import dev.tr7zw.trender.gui.widget.data.InputResult;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

//#if MC >= 12000
//#else
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//#endif

/**
 * A screen for a {@link SyncedGuiDescription}.
 *
 * @param <T> the description type
 */
public class CottonInventoryScreen<T extends SyncedGuiDescription> extends AbstractContainerScreen<T>
        implements CottonScreenImpl {
    private static final VisualLogger LOGGER = new VisualLogger(CottonInventoryScreen.class);
    protected SyncedGuiDescription description;
    @Nullable
    protected WWidget lastResponder = null;
    private final MouseInputHandler<CottonInventoryScreen<T>> mouseInputHandler = new MouseInputHandler<>(this);

    /**
     * Constructs a new screen without a title.
     *
     * @param description the GUI description
     * @param inventory   the player inventory
     * @since 5.2.0
     */
    public CottonInventoryScreen(T description, Inventory inventory) {
        this(description, inventory, CommonComponents.EMPTY);
    }

    /**
     * Constructs a new screen.
     *
     * @param description the GUI description
     * @param inventory   the player inventory
     * @param title       the screen title
     * @since 5.2.0
     */
    public CottonInventoryScreen(T description, Inventory inventory, Component title) {
        super(description, inventory, title);
        this.description = description;
        width = 18 * 9;
        height = 18 * 9;
        this.imageWidth = 18 * 9;
        this.imageHeight = 18 * 9;
        description.getRootPanel().validate(description);
    }

    /**
     * Constructs a new screen without a title.
     *
     * @param description the GUI description
     * @param player      the player
     */
    public CottonInventoryScreen(T description, Player player) {
        this(description, player.getInventory());
    }

    /**
     * Constructs a new screen.
     *
     * @param description the GUI description
     * @param player      the player
     * @param title       the screen title
     */
    public CottonInventoryScreen(T description, Player player, Component title) {
        this(description, player.getInventory(), title);
    }

    /*
     * RENDERING NOTES:
     * 
     * * "width" and "height" are the width and height of the overall screen *
     * "backgroundWidth" and "backgroundHeight" are the width and height of the
     * panel to render * ~~"left" and "top" are *actually* self-explanatory~~ *
     * "left" and "top" are now (1.15) "x" and "y". A bit less self-explanatory, I
     * guess. * coordinates start at 0,0 at the topleft of the screen.
     */

    @Override
    public void init() {
        super.init();

        WPanel root = description.getRootPanel();
        if (root != null)
            root.addPainters();
        description.addPainters();

        reposition(width, height);

        if (root != null) {
            GuiEventListener rootPanelElement = FocusElements.ofPanel(root);
            ((ScreenAccessor) this).libgui$getChildren().add(rootPanelElement);
            setInitialFocus(rootPanelElement);
        } else {
            LOGGER.warn("No root panel found, keyboard navigation disabled");
        }
    }

    @Override
    public void removed() {
        super.removed();
        VisualLogger.reset();
    }

    @ApiStatus.Internal
    @Override
    public GuiDescription getDescription() {
        return description;
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
     * Clears the heavyweight peers of this screen's GUI description.
     */
    private void clearPeers() {
        description.slots.clear();
    }

    /**
     * Repositions the root panel.
     *
     * @param screenWidth  the width of the screen
     * @param screenHeight the height of the screen
     */
    protected void reposition(int screenWidth, int screenHeight) {
        WPanel basePanel = description.getRootPanel();
        if (basePanel != null) {
            clearPeers();
            basePanel.validate(description);

            imageWidth = basePanel.getWidth();
            imageHeight = basePanel.getHeight();

            //DEBUG
            if (imageWidth < 16)
                imageWidth = 300;
            if (imageHeight < 16)
                imageHeight = 300;
        }

        titleLabelX = description.getTitlePos().x();
        titleLabelY = description.getTitlePos().y();

        if (!description.isFullscreen()) {
            leftPos = (screenWidth / 2) - (imageWidth / 2);
            topPos = (screenHeight / 2) - (imageHeight / 2);
        } else {
            leftPos = 0;
            topPos = 0;

            if (basePanel != null) {
                basePanel.setSize(screenWidth, screenHeight);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        //...yeah, we're going to go ahead and override that.
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int containerX = (int) mouseX - leftPos;
        int containerY = (int) mouseY - topPos;
        mouseInputHandler.checkFocus(containerX, containerY);
        if (containerX < 0 || containerY < 0 || containerX >= width || containerY >= height)
            return true;
        mouseInputHandler.onMouseDown(containerX, containerY, mouseButton);

        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);

        int containerX = (int) mouseX - leftPos;
        int containerY = (int) mouseY - topPos;
        mouseInputHandler.onMouseUp(containerX, containerY, mouseButton);

        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
        super.mouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);

        int containerX = (int) mouseX - leftPos;
        int containerY = (int) mouseY - topPos;
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

        int containerX = (int) mouseX - leftPos;
        int containerY = (int) mouseY - topPos;
        mouseInputHandler.onMouseScroll(containerX, containerY, horizontalAmount, verticalAmount);

        return true;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);

        int containerX = (int) mouseX - leftPos;
        int containerY = (int) mouseY - topPos;
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

    @Override
    //#if MC >= 12000
    protected void renderBg(GuiGraphics context, float partialTicks, int mouseX, int mouseY) {
    } //This is just an AbstractContainerScreen thing; most Screens don't work this way.
      //#else
      //$$protected void renderBg(PoseStack context, float partialTicks, int mouseX, int mouseY) {}
      //#endif

    /**
     * Paints the GUI description of this screen.
     *
     * @param context the draw context
     * @param mouseX  the absolute X coordinate of the mouse cursor
     * @param mouseY  the absolute Y coordinate of the mouse cursor
     * @param delta   the tick delta
     * @since 9.2.0
     */
    public void paintDescription(RenderContext context, int mouseX, int mouseY, float delta) {
        if (description != null) {
            WPanel root = description.getRootPanel();
            if (root != null) {
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                Scissors.refreshScissors();
                root.paint(context, leftPos, topPos, mouseX - leftPos, mouseY - topPos);
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                Scissors.checkStackIsEmpty();
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
        Lighting.setupForFlatItems(); //Needed because super.render leaves dirty state

        if (description != null) {
            WPanel root = description.getRootPanel();
            if (root != null) {
                WWidget hitChild = root.hit(mouseX - leftPos, mouseY - topPos);
                if (hitChild != null)
                    hitChild.renderTooltip(renderContext, leftPos, topPos, mouseX - leftPos, mouseY - topPos);
            }
        }

        renderTooltip(context, mouseX, mouseY); //Draws the itemstack tooltips
        VisualLogger.render(renderContext);
    }

    @Override
    //#if MC >= 12000
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        if (description != null && description.isTitleVisible()) {
            int width = description.getRootPanel().getWidth();
            RenderContext renderContext = new RenderContext(context);
            //#else
            //$$ protected void renderLabels(PoseStack context, int mouseX, int mouseY) {
            //$$     if (description != null && description.isTitleVisible()) {
            //$$         int width = description.getRootPanel().getWidth();
            //$$         RenderContext renderContext = new RenderContext(this, context);
            //#endif
            ScreenDrawing.drawString(renderContext, getTitle().getVisualOrderText(), description.getTitleAlignment(),
                    titleLabelX, titleLabelY, width - 2 * titleLabelX, description.getTitleColor());
        }

        // Don't draw the player inventory label as it's drawn by the widget itself
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (description != null) {
            WPanel root = description.getRootPanel();
            if (root != null) {
                root.tick();
            }
        }
    }

    @Override
    protected void updateNarratedWidget(NarrationElementOutput builder) {
        if (description != null)
            NarrationHelper.addNarrations(description.getRootPanel(), builder);
    }
}
