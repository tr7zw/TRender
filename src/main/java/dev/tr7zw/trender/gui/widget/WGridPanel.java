package dev.tr7zw.trender.gui.widget;

import java.util.ArrayList;
import java.util.List;

import dev.tr7zw.trender.gui.GuiDescription;
import dev.tr7zw.trender.gui.impl.VisualLogger;
import dev.tr7zw.trender.gui.widget.data.Insets;

/**
 * A panel that positions children in a grid.
 */
public class WGridPanel extends WPanelWithInsets {
    private static final VisualLogger LOGGER = new VisualLogger(WGridPanel.class);

    /**
     * The grid size in pixels. Defaults to 18, which is the size of one item slot.
     */
    protected int grid = 18;

    /**
     * The horizontal gap between two grid cells.
     * 
     * @since 6.4.0
     */
    protected int horizontalGap = 0;

    /**
     * The vertical gap between two grid cells.
     * 
     * @since 6.4.0
     */
    protected int verticalGap = 0;

    /**
     * The widgets whose host hasn't been set yet.
     */
    private final List<WWidget> requiresHost = new ArrayList<>();

    /**
     * Constructs a grid panel with the default grid size.
     */
    public WGridPanel() {
    }

    /**
     * Constructs a grid panel with a custom grid size.
     *
     * @param gridSize the grid size in pixels
     */
    public WGridPanel(int gridSize) {
        this.grid = gridSize;
    }

    /**
     * Set the gaps between grid cells.
     *
     * <p>
     * <b>This method can only be called before any elements get added to this
     * layout.</b>
     * </p>
     *
     * @param horizontalGap the horizontal gap between grid cells
     * @param verticalGap   the vertical gap between grid cells
     * @since 6.4.0
     */
    public WGridPanel setGaps(int horizontalGap, int verticalGap) {
        if (!this.children.isEmpty()) {
            LOGGER.warn("You can only change gaps before adding children to a WGridPanel");
            return this;
        }
        this.horizontalGap = horizontalGap;
        this.verticalGap = verticalGap;
        return this;
    }

    /**
     * Adds a widget to this panel.
     *
     * <p>
     * If the widget {@linkplain WWidget#canResize() can be resized}, it will be
     * resized to ({@link #grid}, {@link #grid}).
     *
     * @param w the widget
     * @param x the X position in grid cells
     * @param y the Y position in grid cells
     */
    public void add(WWidget w, int x, int y) {
        add(w, x, y, 1, 1);
    }

    /**
     * Adds a widget to this panel and resizes it to a custom size.
     *
     * @param w      the widget
     * @param x      the X position in grid cells
     * @param y      the Y position in grid cells
     * @param width  the new width in grid cells
     * @param height the new height in grid cells
     */
    public void add(WWidget w, int x, int y, int width, int height) {
        children.add(w);
        w.setParent(this);
        w.setLocation(x * (grid + horizontalGap) + insets.left(), y * (grid + verticalGap) + insets.top());
        if (w.canResize()) {
            w.setSize((width - 1) * (grid + horizontalGap) + grid, (height - 1) * (grid + verticalGap) + grid);
        }

        if (host != null) {
            w.setHost(host);
        } else {
            requiresHost.add(w);
        }

        expandToFit(w, insets);
    }

    @Override
    public WGridPanel setInsets(Insets insets) {
        super.setInsets(insets);
        return this;
    }

    @Override
    public void setHost(GuiDescription host) {
        super.setHost(host);
        setRequiredHosts(host);
    }

    private void setRequiredHosts(GuiDescription host) {
        for (WWidget widget : requiresHost) {
            widget.setHost(host);
        }
        requiresHost.clear();
    }
}
