package dev.enderpalm.lignin.mixin.text;

import dev.enderpalm.lignin.text.StyleInjector;
import dev.enderpalm.lignin.text.container.Badge;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Style.class)
public abstract class StyleMixin implements StyleInjector {

    @Mutable @Shadow @Final @Nullable TextColor color;
    @Mutable @Shadow @Final @Nullable Boolean bold, italic, underlined, strikethrough, obfuscated;
    @Mutable @Shadow @Final @Nullable ClickEvent clickEvent;
    @Mutable @Shadow @Final @Nullable HoverEvent hoverEvent;
    @Mutable @Shadow @Final @Nullable String insertion;
    @Mutable @Shadow @Final @Nullable ResourceLocation font;

    @Nullable Badge badge;

    @Override
    public Style duckConstruct(@Nullable Badge badge, @Nullable TextColor textColor, @Nullable Boolean bold, @Nullable Boolean italic, @Nullable Boolean underlined, @Nullable Boolean strikethrough, @Nullable Boolean obfuscated, @Nullable ClickEvent clickEvent, @Nullable HoverEvent hoverEvent, @Nullable String insertion, @Nullable ResourceLocation font) {
        this.badge = badge;
        this.color = textColor;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.strikethrough = strikethrough;
        this.obfuscated = obfuscated;
        this.clickEvent = clickEvent;
        this.hoverEvent = hoverEvent;
        this.insertion = insertion;
        this.font = font;
        return StyleInjector.super.duckConstruct(badge, textColor, bold, italic, underlined, strikethrough, obfuscated, clickEvent, hoverEvent, insertion, font);
    }

    @Override
    public Style withBadge(Badge badge) {
        return this.duckConstruct(badge, this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
    }

    @Override
    public @Nullable Badge getBadge() {
        return this.badge;
    }

    @Inject(method = "withBold", at = @At("RETURN"), cancellable = true)
    private void withBold(@Nullable Boolean bold, @NotNull CallbackInfoReturnable<Style> cir) {
        cir.setReturnValue(this.duckConstruct(this.badge, this.color, bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font));
    }

    @Inject(method = "applyTo", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/chat/Style;<init>(Lnet/minecraft/network/chat/TextColor;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Lnet/minecraft/network/chat/ClickEvent;Lnet/minecraft/network/chat/HoverEvent;Ljava/lang/String;Lnet/minecraft/resources/ResourceLocation;)V"), cancellable = true)
    private void applyTo(Style style, @NotNull CallbackInfoReturnable<Style> cir) {
        cir.setReturnValue(this.duckConstruct(this.badge == null ? style.getBadge() : this.badge,
                this.color == null ? style.getColor() : this.color,
                this.bold == null ? style.isBold() : this.bold,
                this.italic == null ? style.isItalic() : this.italic,
                this.underlined == null ? style.isUnderlined() : this.underlined,
                this.strikethrough == null ? style.isStrikethrough() : this.strikethrough,
                this.obfuscated == null ? style.isObfuscated() : this.obfuscated,
                this.clickEvent == null ? style.getClickEvent() : this.clickEvent,
                this.hoverEvent == null ? style.getHoverEvent() : this.hoverEvent,
                this.insertion == null ? style.getInsertion() : this.insertion,
                this.font == null ? style.getFont() : this.font
        ));
    }

    @Redirect(method = "toString", at = @At(value = "INVOKE",
            target = "Ljava/lang/StringBuilder;append(Ljava/lang/String;)Ljava/lang/StringBuilder;"))
    private @NotNull StringBuilder appendCustomFlag(@NotNull StringBuilder instance, String str) {
        class Collector {
            private boolean isFirst = instance.charAt(instance.length() - 1) == '{';

            void addValueString(String entry, @Nullable Object value) {
                if (value == null) return;
                if (this.isFirst) this.isFirst = false;
                else instance.append(',');
                instance.append(entry).append("=").append(value);
            }
        }
        Collector cl = new Collector();
        cl.addValueString("badge", this.badge);
        return instance.append(str);
    }
}
