package dev.enderpalm.lignin.mixin.text;

import dev.enderpalm.lignin.text.StyleInjector;
import dev.enderpalm.lignin.text.container.Badge;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Style.class)
public abstract class StyleMixin implements StyleInjector {

    @Shadow @Final @Nullable TextColor color;
    @Shadow @Final @Nullable Boolean bold;
    @Shadow @Final @Nullable Boolean italic;
    @Shadow @Final @Nullable Boolean underlined;
    @Shadow @Final @Nullable Boolean strikethrough;
    @Shadow @Final @Nullable Boolean obfuscated;
    @Shadow @Final @Nullable ClickEvent clickEvent;
    @Shadow @Final @Nullable HoverEvent hoverEvent;
    @Shadow @Final @Nullable String insertion;
    @Shadow @Final @Nullable ResourceLocation font;

    @Final @Mutable @Shadow public static Style EMPTY;

    @Nullable Badge badge;

    @Override
    public Style withBadge(@Nullable Badge badge) {
        this.badge = badge;
        return this.setupStatic();
    }

    @Override
    public @Nullable Badge getBadge() {
        return this.badge;
    }

    @Inject(method = "isEmpty", at = @At(value = "RETURN"), cancellable = true)
    private void isEmpty(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(this.bold == null && this.italic == null && this.underlined == null && this.strikethrough == null && this.obfuscated == null && this.clickEvent == null && this.hoverEvent == null && this.insertion == null && this.font == null && this.badge == null);
    }

    @Inject(method = "equals", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void equals(Object object, CallbackInfoReturnable<Boolean> cir){
        var isBadgeEqual = Objects.equals(this.badge, ((Style) object).getBadge());
        cir.setReturnValue(cir.getReturnValue() && isBadgeEqual);
    }

    /**
     * @author Enderpalm
     * @reason Replace <code>this == Style.EMPTY</code>, comparing by value instead of reference
     */
    @Overwrite
    public Style applyTo(Style style){
        var instance = (Style) (Object) this;
        if (instance.isEmpty()) return style;
        else if (style.isEmpty()) return instance;
        else return new Style(this.color == null ? style.getColor() : this.color,
                this.bold == null ? style.isBold() : this.bold,
                this.italic == null ? style.isItalic() : this.italic,
                this.underlined == null ? style.isUnderlined() : this.underlined,
                this.strikethrough == null ? style.isStrikethrough() : this.strikethrough,
                this.obfuscated == null ? style.isObfuscated() : this.obfuscated,
                this.clickEvent == null ? style.getClickEvent() : this.clickEvent,
                this.hoverEvent == null ? style.getHoverEvent() : this.hoverEvent,
                this.insertion == null ? style.getInsertion() : this.insertion,
                this.font == null ? style.getFont() : this.font)
                    .withBadge(this.badge == null ? style.getBadge() : this.badge);
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

    private Style setupStatic(){
        EMPTY = new Style(null, null, null, null, null, null, null, null, null, null);
        return (Style) (Object) this;
    }

    private void appendStyleProperties(@NotNull CallbackInfoReturnable<Style> cir){
        cir.setReturnValue(cir.getReturnValue().withBadge(this.badge));
    }

    @Inject(method = {"applyFormat", "applyLegacyFormat"}, at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void applyFormat(ChatFormatting formatting, CallbackInfoReturnable<Style> cir){
        this.appendStyleProperties(cir);
    }

    @Inject(method = "applyFormats", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void applyMultipleFormat(ChatFormatting[] formats, CallbackInfoReturnable<Style> cir){
        this.appendStyleProperties(cir);
    }

    @Inject(method = "withColor(Lnet/minecraft/network/chat/TextColor;)Lnet/minecraft/network/chat/Style;", at = @At("RETURN"), cancellable = true)
    private void withColor(@Nullable TextColor color, @NotNull CallbackInfoReturnable<Style> cir) {
        this.appendStyleProperties(cir);
    }

    @Inject(method = {"withBold", "withItalic", "withUnderlined", "withStrikethrough", "withObfuscated"}, at = @At("RETURN"), cancellable = true)
    private void withBold(@Nullable Boolean val, @NotNull CallbackInfoReturnable<Style> cir){
        this.appendStyleProperties(cir);
    }

    @Inject(method = "withClickEvent", at = @At("RETURN"), cancellable = true)
    private void withClickEvent(@Nullable ClickEvent clickEvent, @NotNull CallbackInfoReturnable<Style> cir){
        this.appendStyleProperties(cir);
    }

    @Inject(method = "withHoverEvent", at = @At("RETURN"), cancellable = true)
    private void withHoverEvent(@Nullable HoverEvent hoverEvent, @NotNull CallbackInfoReturnable<Style> cir){
        this.appendStyleProperties(cir);
    }

    @Inject(method = "withInsertion", at = @At("RETURN"), cancellable = true)
    private void withInsertion(@Nullable String insertion, @NotNull CallbackInfoReturnable<Style> cir){
        this.appendStyleProperties(cir);
    }

    @Inject(method = "withFont", at = @At("RETURN"), cancellable = true)
    private void withFont(@Nullable ResourceLocation fontId, @NotNull CallbackInfoReturnable<Style> cir){
        this.appendStyleProperties(cir);
    }
}
