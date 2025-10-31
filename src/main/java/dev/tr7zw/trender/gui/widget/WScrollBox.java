package dev.tr7zw.trender.gui.widget;

import dev.tr7zw.transition.mc.*;
import dev.tr7zw.trender.gui.widget.data.*;
import java.util.*;

public class WScrollBox extends WBox {

    private List<WWidget> allChildren = new ArrayList<>();
    private int scrollIndex = 0;

    public WScrollBox(Axis axis) {
        super(axis);
    }

    @Override
    public void add(WWidget widget, int width, int height) {
        allChildren.add(widget);
        super.add(widget, width, height);
    }

    @Override
    public void layout() {
        this.children.clear();
        int maxWidth = getWidth();
        int widthUsed = 0;
        if (scrollIndex > 0) {
            WButton leftButton = new WButton(ComponentProvider.literal("<"));
            leftButton.setOnClick(() -> {
                scrollIndex = Math.max(0, scrollIndex - 1);
                layout();
            });
            children.add(leftButton);
            leftButton.setParent(this);
            widthUsed += leftButton.getWidth() + spacing;
        }
        for (int i = scrollIndex; i < allChildren.size(); i++) {
            if (widthUsed + allChildren.get(i).getWidth() + spacing < maxWidth) {
                this.children.add(allChildren.get(i));
                widthUsed += allChildren.get(i).getWidth() + spacing;
            } else {
                WButton rightButton = new WButton(ComponentProvider.literal(">"));
                rightButton.setOnClick(() -> {
                    scrollIndex = Math.min(children.size() - 1, scrollIndex + 1);
                    layout();
                });
                children.add(rightButton);
                rightButton.setParent(this);
                break;
            }
        }
        super.layout();
    }

}
