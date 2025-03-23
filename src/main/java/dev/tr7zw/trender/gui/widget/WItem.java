package dev.tr7zw.trender.gui.widget;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.common.collect.ImmutableList;
//#if MC < 12105
//$$ import com.mojang.blaze3d.systems.RenderSystem;
//#endif

import dev.tr7zw.trender.gui.client.RenderContext;
import net.minecraft.core.Registry;
//#if MC >= 11904
import net.minecraft.core.registries.BuiltInRegistries;
//#else
//$$ import net.minecraft.data.BuiltinRegistries;
//#endif
//#if MC >= 11800
import net.minecraft.tags.TagKey;
import net.minecraft.core.Holder;
//#endif
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

/**
 * A widget that displays an item or a list of items.
 *
 * @since 1.8.0
 */
public class WItem extends WWidget {
    private List<ItemStack> items;
    private int duration = 25;
    private int ticks = 0;
    private int current = 0;

    public WItem(List<ItemStack> items) {
        setItems(items);
    }

    //#if MC >= 11800
    public WItem(TagKey<? extends ItemLike> tag) {
        this(getRenderStacks(tag));
    }
    //#endif

    public WItem(ItemStack stack) {
        this(Collections.singletonList(stack));
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public void tick() {
        if (ticks++ >= duration) {
            ticks = 0;
            current = (current + 1) % items.size();
        }
    }

    @Override
    public void paint(RenderContext context, int x, int y, int mouseX, int mouseY) {
        //#if MC < 12105
        //$$ RenderSystem.enableDepthTest();
        //#endif
        context.renderFakeItem(items.get(current), x + getWidth() / 2 - 8, y + getHeight() / 2 - 8);
    }

    /**
     * Returns the animation duration of this {@code WItem}.
     *
     * <p>
     * Defaults to 25 screen ticks.
     */
    public int getDuration() {
        return duration;
    }

    public WItem setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    /**
     * Sets the item list of this {@code WItem} and resets the animation state.
     *
     * @param items the new item list
     * @return this instance
     */
    public WItem setItems(List<ItemStack> items) {
        Objects.requireNonNull(items, "stacks == null!");
        if (items.isEmpty())
            throw new IllegalArgumentException("The stack list is empty!");

        this.items = items;

        // Reset the state
        current = 0;
        ticks = 0;

        return this;
    }

    /**
     * Gets the default stacks ({@link Item#getDefaultInstance()} ()}) of each item
     * in a tag.
     */
    //#if MC >= 11800
    @SuppressWarnings("unchecked")
    private static List<ItemStack> getRenderStacks(TagKey<? extends ItemLike> tag) {
        //#if MC >= 12103
        Registry<ItemLike> registry = (Registry<ItemLike>) BuiltInRegistries.REGISTRY
                .getValue(tag.registry().location());
        //#elseif MC >= 11904
        //$$Registry<ItemLike> registry = (Registry<ItemLike>) BuiltInRegistries.REGISTRY
        //$$        .get(tag.registry().location());
        //#else
        //$$Registry<ItemLike> registry = (Registry<ItemLike>) BuiltinRegistries.REGISTRY
        //$$        .get(tag.registry().location());
        //#endif
        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();

        for (Holder<ItemLike> item : registry.getTagOrEmpty((TagKey<ItemLike>) tag)) {
            builder.add(item.value().asItem().getDefaultInstance());
        }

        return builder.build();
    }
    //#endif
}
