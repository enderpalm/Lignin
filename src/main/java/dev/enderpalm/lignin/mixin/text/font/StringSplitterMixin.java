package dev.enderpalm.lignin.mixin.text.font;

import dev.enderpalm.lignin.text.container.Badge;
import dev.enderpalm.lignin.text.container.FontVariant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.StringSplitter;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(StringSplitter.class)
public abstract class StringSplitterMixin {

    private Badge prevBadge;
    private boolean isNotLineStart;

    @Redirect(method = "method_27496", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/StringSplitter$WidthProvider;getWidth(ILnet/minecraft/network/chat/Style;)F"))
    private float appendStringWidth(StringSplitter.WidthProvider instance, int i, Style style){
        return this.getModifiedGlyphWidth(instance, i, style);
    }

    @Redirect(method = "method_27492", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/StringSplitter$WidthProvider;getWidth(ILnet/minecraft/network/chat/Style;)F"))
    private float appendFormattedTextWidth(StringSplitter.WidthProvider instance, int i, Style style) {
        return this.getModifiedGlyphWidth(instance, i, style);
    }

    @Redirect(method = "method_30879", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/StringSplitter$WidthProvider;getWidth(ILnet/minecraft/network/chat/Style;)F"))
    private float appendFormattedCharSequenceWidth(StringSplitter.WidthProvider instance, int i, Style style){
        return this.getModifiedGlyphWidth(instance, i, style);
    }

    private float getModifiedGlyphWidth(StringSplitter.WidthProvider instance, int codePoint, @NotNull Style style){
        style = FontVariant.getSwitchedFont(style);
        float glyphWidth = instance.getWidth(codePoint, style) + Badge.renderOffset(this.prevBadge, style.getBadge(), this.isNotLineStart);
        this.prevBadge = style.getBadge();
        this.isNotLineStart = true;
        return glyphWidth;
    }
}
