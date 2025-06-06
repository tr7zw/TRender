package dev.tr7zw.trender.gui.widget;

import java.util.Objects;

import dev.tr7zw.trender.gui.GuiDescription;
import dev.tr7zw.trender.gui.TriState;
import dev.tr7zw.trender.gui.client.BackgroundPainter;
import dev.tr7zw.trender.gui.client.RenderContext;
import dev.tr7zw.trender.gui.client.Scissors;
import dev.tr7zw.trender.gui.widget.data.Axis;
import dev.tr7zw.trender.gui.widget.data.InputResult;
import dev.tr7zw.trender.gui.widget.data.Insets;

/**
 * Similar to the JScrollPane in Swing, this widget represents a scrollable
 * widget.
 *
 * @since 2.0.0
 */
public class WScrollPanel extends WPanel {
    private static final int SCROLL_BAR_SIZE = 8;
    private final WWidget widget;

    private TriState scrollingHorizontally = TriState.DEFAULT;
    private TriState scrollingVertically = TriState.DEFAULT;

    /**
     * The horizontal scroll bar of this panel.
     */
    protected WScrollBar horizontalScrollBar = new WScrollBar(Axis.HORIZONTAL);

    /**
     * The vertical scroll bar of this panel.
     */
    protected WScrollBar verticalScrollBar = new WScrollBar(Axis.VERTICAL);

    private int lastHorizontalScroll = -1;
    private int lastVerticalScroll = -1;

    private Insets insets = Insets.NONE;

    /**
     * Creates a vertically scrolling panel.
     *
     * @param widget the viewed widget
     */
    public WScrollPanel(WWidget widget) {
        this.widget = widget;

        widget.setParent(this);
        horizontalScrollBar.setParent(this);
        verticalScrollBar.setParent(this);

        children.add(widget);
        children.add(verticalScrollBar); // Only vertical scroll bar
    }

    /**
     * Returns this scroll panel's horizontal scroll bar.
     *
     * @return the horizontal scroll bar
     * @since 9.1.0
     */
    public WScrollBar getHorizontalScrollBar() {
        return this.horizontalScrollBar;
    }

    /**
     * Sets this scroll panel's horizontal scroll bar.
     *
     * @param horizontalScrollBar the new horizontal scroll bar
     * @return this scroll panel
     * @throws NullPointerException if the horizontalScrollBar is null
     * @since 9.1.0
     */
    public WScrollPanel setHorizontalScrollBar(WScrollBar horizontalScrollBar) {
        this.horizontalScrollBar = Objects.requireNonNull(horizontalScrollBar, "horizontalScrollBar");
        return this;
    }

    /**
     * Returns whether this scroll panel has a horizontal scroll bar.
     *
     * @return true if there is a horizontal scroll bar, default if a scroll bar
     *         should be added if needed, and false otherwise
     */
    public TriState isScrollingHorizontally() {
        return scrollingHorizontally;
    }

    public WScrollPanel setScrollingHorizontally(TriState scrollingHorizontally) {
        if (scrollingHorizontally != this.scrollingHorizontally) {
            this.scrollingHorizontally = scrollingHorizontally;
            layout();
        }

        return this;
    }

    /**
     * Returns this scroll panel's vertical scroll bar.
     *
     * @return the vertical scroll bar
     * @since 9.1.0
     */
    public WScrollBar getVerticalScrollBar() {
        return this.verticalScrollBar;
    }

    /**
     * Sets this scroll panel's vertical scroll bar.
     *
     * @param verticalScrollBar the new vertical scroll bar
     * @return this scroll panel
     * @throws NullPointerException if the verticalScrollBar is null
     * @since 9.1.0
     */
    public WScrollPanel setVerticalScrollBar(WScrollBar verticalScrollBar) {
        this.verticalScrollBar = Objects.requireNonNull(verticalScrollBar, "verticalScrollBar");
        return this;
    }

    /**
     * Returns whether this scroll panel has a vertical scroll bar.
     *
     * @return true if there is a vertical scroll bar, * default if a scroll bar
     *         should be added if needed, * and false otherwise
     */
    public TriState isScrollingVertically() {
        return scrollingVertically;
    }

    public WScrollPanel setScrollingVertically(TriState scrollingVertically) {
        if (scrollingVertically != this.scrollingVertically) {
            this.scrollingVertically = scrollingVertically;
            layout();
        }

        return this;
    }

    @Override
    public void paint(RenderContext context, int x, int y, int mouseX, int mouseY) {
        if (verticalScrollBar.getValue() != lastVerticalScroll
                || horizontalScrollBar.getValue() != lastHorizontalScroll) {
            layout();
            lastHorizontalScroll = horizontalScrollBar.getValue();
            lastVerticalScroll = verticalScrollBar.getValue();
        }

        BackgroundPainter backgroundPainter = getBackgroundPainter();
        if (backgroundPainter != null)
            backgroundPainter.paintBackground(context, x, y, this);

        Insets insets = getInsets();
        for (WWidget child : children) {
            if (child == widget) {
                Scissors.push(context, x + insets.left(), y + insets.top(), getWidth() - insets.width(),
                        getHeight() - insets.height());
            }

            child.paint(context, x + child.getX(), y + child.getY(), mouseX - child.getX(), mouseY - child.getY());

            if (child == widget) {
                Scissors.pop();
            }
        }
    }

    @Override
    public void layout() {
        children.clear();

        boolean horizontal = hasHorizontalScrollbar();
        boolean vertical = hasVerticalScrollbar();

        int offset = (horizontal && vertical) ? SCROLL_BAR_SIZE : 0;
        verticalScrollBar.setSize(SCROLL_BAR_SIZE, this.getHeight() - offset);
        verticalScrollBar.setLocation(this.getWidth() - verticalScrollBar.getWidth(), 0);
        horizontalScrollBar.setSize(this.getWidth() - offset, SCROLL_BAR_SIZE);
        horizontalScrollBar.setLocation(0, this.getHeight() - horizontalScrollBar.getHeight());

        if (widget instanceof WPanel panel)
            panel.layout();
        widget.refresh();
        children.add(widget);
        Insets insets = getInsets();
        int x = insets.left() + (horizontal ? -horizontalScrollBar.getValue() : 0);
        int y = insets.top() + (vertical ? -verticalScrollBar.getValue() : 0);
        widget.setLocation(x, y);

        verticalScrollBar.setWindow(this.getHeight() - insets.height() - (horizontal ? SCROLL_BAR_SIZE : 0));
        verticalScrollBar.setMaxValue(widget.getHeight());
        horizontalScrollBar.setWindow(this.getWidth() - insets.width() - (vertical ? SCROLL_BAR_SIZE : 0));
        horizontalScrollBar.setMaxValue(widget.getWidth());

        if (vertical)
            children.add(verticalScrollBar);
        if (horizontal)
            children.add(horizontalScrollBar);
    }

    private boolean hasHorizontalScrollbar() {
        return (scrollingHorizontally == TriState.DEFAULT) ? (widget.getWidth() > this.getWidth() - SCROLL_BAR_SIZE)
                : scrollingVertically.withDefault(false);
    }

    private boolean hasVerticalScrollbar() {
        return (scrollingVertically == TriState.DEFAULT) ? (widget.getHeight() > this.getHeight() - SCROLL_BAR_SIZE)
                : scrollingVertically.withDefault(false);
    }

    @Override
    public InputResult onMouseScroll(int x, int y, double horizontalAmount, double verticalAmount) {
        var horizontalResult = InputResult.IGNORED;
        var verticalResult = InputResult.IGNORED;

        if (hasHorizontalScrollbar()) {
            horizontalResult = horizontalScrollBar.onMouseScroll(x, y, horizontalAmount, 0);
        }

        if (hasVerticalScrollbar()) {
            verticalResult = verticalScrollBar.onMouseScroll(0, 0, 0, verticalAmount);
        }

        return horizontalResult.or(verticalResult);
    }

    @Override
    public void validate(GuiDescription c) {
        //you have to validate these ones manually since they are not in children list
        this.horizontalScrollBar.validate(c);
        this.verticalScrollBar.validate(c);
        super.validate(c);
    }

    /**
     * {@return the layout insets used for the viewed widget}
     * 
     * @since 9.1.0
     */
    public Insets getInsets() {
        return insets;
    }

    /**
     * Sets the layout insets used for the viewed widget.
     *
     * @param insets the layout insets
     * @return this scroll panel
     * @since 9.1.0
     */
    public WScrollPanel setInsets(Insets insets) {
        this.insets = Objects.requireNonNull(insets, "Insets cannot be null");
        return this;
    }
}
