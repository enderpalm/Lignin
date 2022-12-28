package dev.enderpalm.lignin.mixin.text;

import dev.enderpalm.lignin.text.decorator.Decoration;
import dev.enderpalm.lignin.text.decorator.Decorator;
import dev.enderpalm.lignin.text.decorator.enums.DropShadow;
import dev.enderpalm.lignin.text.decorator.enums.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MutableComponent.class)
public abstract class MutableComponentMixin implements Component, Decorator {

    private Decoration decoration;

    @Override
    public Component addDecoration(Decoration decoration) {
        this.decoration = decoration;
        return this;
    }

    @Override
    public Component fillSolid(int color, int shadowColor, DropShadow dropShadow, int borderRadius) {
        return this.addDecoration(new Decoration(Style.FILL_SOLID, color, shadowColor, borderRadius, dropShadow));
    }

    @Override
    public Decoration getDecoration() {
        return this.decoration;
    }
}
