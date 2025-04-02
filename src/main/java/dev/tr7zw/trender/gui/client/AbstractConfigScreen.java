package dev.tr7zw.trender.gui.client;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import dev.tr7zw.trender.gui.client.AbstractConfigScreen.OptionInstance.SplitLine;
import dev.tr7zw.trender.gui.widget.WButton;
import dev.tr7zw.trender.gui.widget.WGridPanel;
import dev.tr7zw.trender.gui.widget.WLabel;
import dev.tr7zw.trender.gui.widget.WLabeledDoubleSlider;
import dev.tr7zw.trender.gui.widget.WLabeledIntSlider;
import dev.tr7zw.trender.gui.widget.WListPanel;
import dev.tr7zw.trender.gui.widget.WToggleButton;
import dev.tr7zw.trender.gui.widget.data.Insets;
import dev.tr7zw.transition.nms.ComponentProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class AbstractConfigScreen extends LightweightGuiDescription {

    protected final static DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("###.##");
    private final Screen previous;
    private final Component title;

    public AbstractConfigScreen(Component title, Screen previous) {
        this.previous = previous;
        this.title = title;
    }

    public Screen createScreen() {
        return new CottonClientScreen(title, this) {
            @Override
            public void onClose() {
                save();
                this.minecraft.setScreen(previous);
            }
        };
    }

    public abstract void save();

    public abstract void reset();

    public WListPanel<OptionInstance, WGridPanel> createOptionList(List<OptionInstance> options) {
        return new WListPanel<OptionInstance, WGridPanel>(options, () -> new WGridPanel(), (option, panel) -> {
            panel.setInsets(new Insets(0, 2, 0, 0));
            switch (option) {
            case OptionInstance.Toggle toggle -> {
                WToggleButton tb = new WToggleButton(ComponentProvider.translatable(toggle.translationKey()));
                tb.setToggle(toggle.current.get());
                tb.setOnRefresh(() -> tb.setToggle(toggle.current.get()));
                tb.setOnToggle(toggle.update);
                tb.setToolip(getOptionalTooltip(toggle.translationKey()));
                panel.add(tb, 0, 0, 10, 1);
            }
            case OptionInstance.DoubleOption dblo -> {
                WLabeledDoubleSlider slider = new WLabeledDoubleSlider(dblo.min(), dblo.max(), dblo.steps());
                slider.setValue(dblo.current().getAsDouble());
                slider.setOnRefresh(() -> {
                    slider.setValue(dblo.current().getAsDouble());
                    slider.setLabel(slider.getLabelUpdater().updateLabel(slider.getValue()));
                });
                slider.setValueChangeListener(dblo.update());
                slider.setLabelUpdater(d -> ComponentProvider.translatable(dblo.translationKey())
                        .append(": " + DECIMAL_FORMATTER.format(d)));
                slider.setToolip(getOptionalTooltip(dblo.translationKey()));
                slider.setIgnoreScrolling(true);
                panel.add(slider, 0, 0, 10, 1);
            }
            case OptionInstance.IntOption dblo -> {
                WLabeledIntSlider slider = new WLabeledIntSlider(dblo.min(), dblo.max());
                slider.setValue(dblo.current().getAsInt());
                slider.setOnRefresh(() -> {
                    slider.setValue(dblo.current().getAsInt());
                    slider.setLabel(slider.getLabelUpdater().updateLabel(slider.getValue()));
                });
                slider.setValueChangeListener(dblo.update());
                slider.setLabelUpdater(d -> ComponentProvider.translatable(dblo.translationKey()).append(": " + d));
                slider.setToolip(getOptionalTooltip(dblo.translationKey()));
                slider.setIgnoreScrolling(true);
                panel.add(slider, 0, 0, 10, 1);
            }
            case OptionInstance.EnumOption<?> enm -> {
                WButton tb = new WButton(
                        ComponentProvider.translatable(enm.translationKey() + "." + enm.current().get().name()));
                tb.setOnClick(() -> {
                    Enum cur = enm.current().get();
                    List<Enum<?>> values = Arrays.asList(enm.targetEnum().getEnumConstants());
                    int id = (values.indexOf(cur) + 1) % values.size();
                    enm.update().accept(values.get(id));
                    tb.setLabel(
                            ComponentProvider.translatable(enm.translationKey() + "." + enm.current().get().name()));
                });
                tb.setOnRefresh(() -> tb.setLabel(
                        ComponentProvider.translatable(enm.translationKey() + "." + enm.current().get().name())));
                tb.setToolip(getOptionalTooltip(enm.translationKey()));
                WLabel label = new WLabel(ComponentProvider.translatable(enm.translationKey()));
                panel.setGaps(-13, -13);
                panel.add(label, 0, 1);
                panel.add(tb, (int) (Minecraft.getInstance().font.width(label.getText()) / 4.7), 0, 18, 1);
            }
            case SplitLine line -> {
                WLabel label = new WLabel(ComponentProvider.translatable(line.translationKey()));
                panel.setInsets(new Insets(6, 3, 0, 0));
                panel.add(label, 0, 0, 10, 1);
            }
            }
        });
    }

    public OptionInstance getBooleanOption(String translationKey, Supplier<Boolean> current, Consumer<Boolean> update) {
        return new OptionInstance.Toggle(translationKey, current, update);
    }

    public OptionInstance getOnOffOption(String translationKey, Supplier<Boolean> current, Consumer<Boolean> update) {
        return getBooleanOption(translationKey, current, update);
    }

    public OptionInstance getDoubleOption(String translationKey, double min, double max, double steps,
            DoubleSupplier current, DoubleConsumer update) {
        return new OptionInstance.DoubleOption(translationKey, min, max, steps, current, update);
    }

    public OptionInstance getIntOption(String translationKey, int min, int max, IntSupplier current,
            IntConsumer update) {
        return new OptionInstance.IntOption(translationKey, min, max, current, update);
    }

    public <T extends Enum> OptionInstance getEnumOption(String translationKey, Class<T> targetEnum,
            Supplier<T> current, Consumer<T> update) {
        return new OptionInstance.EnumOption(translationKey, targetEnum, current, update);
    }

    public SplitLine getSplitLine(String translationKey) {
        return new OptionInstance.SplitLine(translationKey);
    }

    private static Component getOptionalTooltip(String translationKey) {
        String key = translationKey + ".tooltip";
        Component comp = ComponentProvider.translatable(key);
        if (key.equals(comp.getString())) {
            return null;
        } else {
            return comp;
        }
    }

    public static sealed interface OptionInstance {
        public record Toggle(String translationKey, Supplier<Boolean> current, Consumer<Boolean> update)
                implements OptionInstance {
        }

        public record DoubleOption(String translationKey, double min, double max, double steps, DoubleSupplier current,
                DoubleConsumer update) implements OptionInstance {
        }

        public record IntOption(String translationKey, int min, int max, IntSupplier current, IntConsumer update)
                implements OptionInstance {
        }

        public record EnumOption<T extends Enum>(String translationKey, Class<T> targetEnum, Supplier<T> current,
                Consumer<Object> update) implements OptionInstance {
        }

        public record SplitLine(String translationKey) implements OptionInstance {
        }
    }
}
