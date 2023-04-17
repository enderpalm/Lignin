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

    @Mutable @Shadow @Final public static Style EMPTY;

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

    @Inject(method = "withColor(Lnet/minecraft/network/chat/TextColor;)Lnet/minecraft/network/chat/Style;", at = @At("RETURN"), cancellable = true)
    private void withColor(@Nullable TextColor color, @NotNull CallbackInfoReturnable<Style> cir) {
        cir.setReturnValue(cir.getReturnValue().withBadge(this.badge));
    }

    @Inject(method = {"withBold", "withItalic", "withUnderlined", "withStrikethrough", "withObfuscated"}, at = @At("RETURN"), cancellable = true)
    private void withBold(@Nullable Boolean val, @NotNull CallbackInfoReturnable<Style> cir){
        cir.setReturnValue(cir.getReturnValue().withBadge(this.badge));
    }

    @Inject(method = "withClickEvent", at = @At("RETURN"), cancellable = true)
    private void withClickEvent(@Nullable ClickEvent clickEvent, @NotNull CallbackInfoReturnable<Style> cir){
        cir.setReturnValue(cir.getReturnValue().withBadge(this.badge));
    }

    @Inject(method = "withHoverEvent", at = @At("RETURN"), cancellable = true)
    private void withHoverEvent(@Nullable HoverEvent hoverEvent, @NotNull CallbackInfoReturnable<Style> cir){
        cir.setReturnValue(cir.getReturnValue().withBadge(this.badge));
    }

    @Inject(method = "withInsertion", at = @At("RETURN"), cancellable = true)
    private void withInsertion(@Nullable String insertion, @NotNull CallbackInfoReturnable<Style> cir){
        cir.setReturnValue(cir.getReturnValue().withBadge(this.badge));
    }

    @Inject(method = "withFont", at = @At("RETURN"), cancellable = true)
    private void withFont(@Nullable ResourceLocation fontId, @NotNull CallbackInfoReturnable<Style> cir){
        cir.setReturnValue(cir.getReturnValue().withBadge(this.badge));
    }
}
