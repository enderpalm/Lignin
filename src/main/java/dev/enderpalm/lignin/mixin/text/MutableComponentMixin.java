package dev.enderpalm.lignin.mixin.text;

import dev.enderpalm.lignin.text.badge.duck.Badge;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MutableComponent.class)
public abstract class MutableComponentMixin implements Component, Badge {

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
