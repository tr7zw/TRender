package dev.tr7zw.trender.gui.impl.client;

//#if MC >= 11800
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
//#endif

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.tr7zw.trender.gui.widget.WPanel;
import dev.tr7zw.trender.gui.widget.WWidget;
import dev.tr7zw.transition.mc.ComponentProvider;

public final class NarrationHelper {
    //#if MC >= 11800
    public static void addNarrations(WPanel rootPanel, NarrationElementOutput builder) {
        List<WWidget> narratableWidgets = getAllWidgets(rootPanel).filter(WWidget::isNarratable)
                .collect(Collectors.toList());

        for (int i = 0, childCount = narratableWidgets.size(); i < childCount; i++) {
            WWidget child = narratableWidgets.get(i);
            if (!child.isFocused() && !child.isHovered())
                continue;

            // replicates Screen.addElementNarrations
            if (narratableWidgets.size() > 1) {
                builder.add(NarratedElementType.POSITION, ComponentProvider
                        .translatable(NarrationMessages.Vanilla.SCREEN_POSITION_KEY, i + 1, childCount));

                if (child.isFocused()) {
                    builder.add(NarratedElementType.USAGE, NarrationMessages.Vanilla.COMPONENT_LIST_USAGE);
                }
            }

            child.addNarrations(builder.nest());
        }
    }
    //#endif

    private static Stream<WWidget> getAllWidgets(WPanel panel) {
        return Stream.concat(Stream.of(panel), panel.streamChildren().flatMap(widget -> {
            if (widget instanceof WPanel nested) {
                return getAllWidgets(nested);
            }

            return Stream.of(widget);
        }));
    }

}
