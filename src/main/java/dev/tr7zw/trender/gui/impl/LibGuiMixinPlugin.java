package dev.tr7zw.trender.gui.impl;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public final class LibGuiMixinPlugin implements IMixinConfigPlugin {
    //    private static final Supplier<Boolean> IS_DEVELOPMENT = Suppliers
    //            .memoize(() -> FabricLoader.getInstance().isDevelopmentEnvironment());

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return switch (mixinClassName) {
        //        case "dev.tr7zw.trender.gui.impl.mixin.client.MinecraftClientMixin" -> IS_DEVELOPMENT.get();
        default -> true;
        };
    }

    // Boilerplate below

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
