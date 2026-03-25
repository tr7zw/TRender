package dev.tr7zw.trender.gui.client;

import dev.tr7zw.transition.mc.*;
import dev.tr7zw.trender.gui.client.AbstractConfigScreen.OptionInstance.*;
import dev.tr7zw.trender.gui.widget.*;
import dev.tr7zw.trender.gui.widget.data.*;
import java.text.*;
import java.util.*;
import java.util.function.*;

import dev.tr7zw.trender.gui.widget.icon.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

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

    public WGridPanel createItemTab(Function<Map.Entry<ResourceKey<Item>, Item>, Boolean> isToggled,
            BiConsumer<Boolean, Map.Entry<ResourceKey<Item>, Item>> toggleHandler) {
        List<Map.Entry<ResourceKey<Item>, Item>> items = new ArrayList<>(ItemUtil.getItems());
        //? if >= 1.21.11 {
        items.sort(Comparator.comparing(a -> a.getKey().identifier().toString()));
        //? } else {
        /*
        items.sort((a, b) -> a.getKey().location().toString().compareTo(b.getKey().location().toString()));
        *///? }
        WListPanel<Map.Entry<ResourceKey<Item>, Item>, WToggleButton> itemList = new WListPanel<Map.Entry<ResourceKey<Item>, Item>, WToggleButton>(
                items, () -> new WToggleButton(ComponentProvider.EMPTY), (s, l) -> {
                    l.setLabel(getItemName(s));
                    l.setToolip(ComponentProvider.literal(getKeyAsString(s)));
                    l.setIcon(new ItemIcon(s.getValue()));
                    l.setToggle(isToggled.apply(s));
                    l.setOnToggle(b -> toggleHandler.accept(b, s));
                });
        itemList.setGap(-1);
        itemList.setInsets(new Insets(2, 4));
        WGridPanel itemTab = new WGridPanel(20);
        itemTab.add(itemList, 0, 0, 17, 7);
        WTextField searchField = new WTextField();
        searchField.setChangedListener(s -> {
            itemList.setFilter(e -> getFilterStringItem(e).toLowerCase().contains(s.toLowerCase()));
            itemList.layout();
        });
        itemTab.add(searchField, 0, 7, 17, 1);
        return itemTab;
    }

    private static Component getItemName(Map.Entry<ResourceKey<Item>, Item> entry) {
        try {
            return entry.getValue().getName(entry.getValue().getDefaultInstance());
        } catch (Exception ex) {
            return ComponentProvider.literal(getKeyAsString(entry));
        }
    }

    private static @NotNull String getKeyAsString(Map.Entry<ResourceKey<Item>, Item> entry) {
        return entry.getKey()/*? >= 1.21.11 {*/.identifier() /*?} else {*//* .location() *//*?}*/.toString();
    }

    private static @NotNull String getFilterStringItem(Map.Entry<ResourceKey<Item>, Item> entry) {
        return getKeyAsString(entry) + " " + getItemName(entry);
    }

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
