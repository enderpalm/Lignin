package dev.enderpalm.lignin.mixin.text.font.provider;

import com.google.gson.JsonObject;
import dev.enderpalm.lignin.text.FontEmphases;
import net.minecraft.client.gui.font.providers.BitmapProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.enderpalm.lignin.text.FontEmphases.deserializeLocation;
import static dev.enderpalm.lignin.text.FontEmphases.deserializeEffectOffset;

@Mixin(BitmapProvider.Builder.class)
public abstract class BitmapProviderBuilderMixin {

    @Inject(method = "fromJson", at = @At("HEAD"))
    private static void appendVariantKeys(JsonObject json, CallbackInfoReturnable<BitmapProvider.Builder> cir) {
        var src = deserializeLocation(json, "src");
        if (json.has("emphases")) {
            var emphasesObj = json.getAsJsonObject("emphases");
            @Nullable ResourceLocation bold = deserializeLocation(emphasesObj, "bold");
            @Nullable ResourceLocation italic = deserializeLocation(emphasesObj, "italic");
            @Nullable FontEmphases.EffectOffset strikethrough = deserializeEffectOffset(emphasesObj, "strikethrough");
            @Nullable FontEmphases.EffectOffset underlined = deserializeEffectOffset(emphasesObj, "underlined");
            FontEmphases.EMPHASES_MAP.put(src, new FontEmphases.Emphases(bold, italic, strikethrough, underlined));
        }
    }
}
