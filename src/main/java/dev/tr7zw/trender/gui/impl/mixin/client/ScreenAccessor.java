package dev.tr7zw.trender.gui.impl.mixin.client;

import net.minecraft.client.*;
import net.minecraft.network.chat.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.*;

import java.util.List;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;

@Mixin(Screen.class)
public interface ScreenAccessor {
    @Accessor("children")
    List<GuiEventListener> libgui$getChildren();

    //? if >= 1.21.11 {
    @Invoker("defaultHandleGameClickEvent")
    void libgui$defaultHandleGameClickEvent(ClickEvent clickEvent, Minecraft minecraft, Screen screen);
    //? }
}
