package dev.enderpalm.lignin.mixin.text.style;

import dev.enderpalm.lignin.text.StyleInjector;
import dev.enderpalm.lignin.text.container.Badge;
import dev.enderpalm.lignin.util.color.OpaqueRGB;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Style.class)
public abstract class StyleMixin implements StyleInjector{

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

    @Nullable private Badge badge;
    @Nullable private OpaqueRGB outline;

    @Override
    public Style withBadge(@Nullable Badge badge) {
        var obj = new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
        obj.innerBindPropertiesToStyle(badge, this.outline);
        return obj;
    }

    @Override
    public @Nullable Badge getBadge() {
        return this.badge;
    }

    @Override
    public Style withOutline(@Nullable OpaqueRGB outline) {
        var obj = new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
        obj.innerBindPropertiesToStyle(this.badge, outline);
        return obj;
    }

    @Override
    public @Nullable OpaqueRGB getOutline() {
        return this.outline;
    }

    @Override
    public void innerBindPropertiesToStyle(@Nullable Badge badge, @Nullable OpaqueRGB outline) {
        this.badge = badge;
        this.outline = outline;
    }

    @Inject(method = "isEmpty", at = @At(value = "RETURN"), cancellable = true)
    private void isEmpty(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(this.color == null && this.bold == null && this.italic == null && this.underlined == null && this.strikethrough == null && this.obfuscated == null && this.clickEvent == null && this.hoverEvent == null && this.insertion == null && this.font == null && this.badge == null && this.outline == null);
    }

    @Inject(method = "equals", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private void equals(Object object, CallbackInfoReturnable<Boolean> cir){
        var isBadgeEqual = Objects.equals(this.badge, ((Style) object).getBadge());
        var isOutlineEqual = Objects.equals(this.outline, ((Style) object).getOutline());
        cir.setReturnValue(cir.getReturnValue() && isBadgeEqual && isOutlineEqual);
    }

    /**
     * @Author Enderpalm
     * Rewrite of {@link Style#applyTo(Style)} to use comparing with value instead of reference
     * @param style Targeted style to apply to
     * @return new instance of style with this instance's values applied to target style's values
     * @reason When adding properties to {@link Style}, object is not newly initialized like Vanilla's methods.
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
                    .withBadge(this.badge == null ? style.getBadge() : this.badge)
                    .withOutline(this.outline == null ? style.getOutline() : this.outline);
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
        cl.addValueString("outline", this.outline);
        return instance.append(str);
    }

    private void appendStyleProperties(@NotNull CallbackInfoReturnable<Style> cir){
        cir.setReturnValue(cir.getReturnValue().withBadge(this.badge).withOutline(this.outline));
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
