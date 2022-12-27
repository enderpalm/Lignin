package dev.enderpalm.lignin.mixin.text;

import dev.enderpalm.lignin.text.badge.duck.Badge;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(MutableComponent.class)
public abstract class MutableComponentMixin implements Component, Badge {

    @Shadow @Final private ComponentContents contents;
    @Shadow @Final private List<Component> siblings;
    @Shadow private Style style;

    private int color;

    @Override
    public Component fillSolid(int color) {
        this.color = color;
        return this;
    }

    @Override
    public int getColor() {
        return this.color;
    }
}
