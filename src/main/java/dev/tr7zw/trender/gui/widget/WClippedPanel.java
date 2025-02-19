package dev.tr7zw.trender.gui.widget;

import dev.tr7zw.trender.gui.client.RenderContext;
import dev.tr7zw.trender.gui.client.Scissors;

/**
 * A panel that is clipped to only render widgets inside its bounds.
 */
public class WClippedPanel extends WPanel {

    @Override
    public void paint(RenderContext context, int x, int y, int mouseX, int mouseY) {
        if (getBackgroundPainter() != null)
            getBackgroundPainter().paintBackground(context, x, y, this);

        Scissors.push(context, x, y, getWidth(), getHeight());
        for (WWidget child : children) {
            child.paint(context, x + child.getX(), y + child.getY(), mouseX - child.getX(), mouseY - child.getY());
        }
        Scissors.pop();
    }
}
