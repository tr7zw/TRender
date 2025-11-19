package dev.tr7zw.trender.gui.widget;

import com.google.common.collect.*;
import dev.tr7zw.trender.gui.client.*;
import java.util.*;
import net.minecraft.core.*;
import net.minecraft.data.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
//? if >= 1.18.0 {
import net.minecraft.tags.*;
//? }
//? if >= 1.19.4 {
import net.minecraft.core.registries.*;
//? }
//? if < 1.21.5 {
/*
import com.mojang.blaze3d.systems.*;
*///? }

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

    //? if >= 1.18.0 {

    public WItem(TagKey<? extends ItemLike> tag) {
        this(getRenderStacks(tag));
    }
    //? }

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
        //? if < 1.21.5 {
        /*
        RenderSystem.enableDepthTest();
        *///? }
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
    //? if >= 1.18.0 {

    @SuppressWarnings("unchecked")
    private static List<ItemStack> getRenderStacks(TagKey<? extends ItemLike> tag) {
        //? if >= 1.21.11 {

        Registry<ItemLike> registry = (Registry<ItemLike>) BuiltInRegistries.REGISTRY
                .getValue(tag.registry().identifier());
        //? } else if >= 1.21.3 {
        /*
        Registry<ItemLike> registry = (Registry<ItemLike>) BuiltInRegistries.REGISTRY
                .getValue(tag.registry().location());
        *///? } else if >= 1.19.4 {
        /*
        Registry<ItemLike> registry = (Registry<ItemLike>) BuiltInRegistries.REGISTRY.get(tag.registry().location());
        *///? } else {
        /*
        Registry<ItemLike> registry = (Registry<ItemLike>) BuiltinRegistries.REGISTRY.get(tag.registry().location());
        *///? }
        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();

        for (Holder<ItemLike> item : registry.getTagOrEmpty((TagKey<ItemLike>) tag)) {
            builder.add(item.value().asItem().getDefaultInstance());
        }

        return builder.build();
    }
    //? }
}
