package dev.enderpalm.lignin.mixin.text.font.provider;

import com.google.gson.JsonObject;
import dev.enderpalm.lignin.text.BitmapProviderBuilderCast;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.providers.BitmapProvider;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
@Mixin(BitmapProvider.Builder.class)
public abstract class BitmapProviderBuilderMixin implements BitmapProviderBuilderCast {

    private static final HashMap<ResourceLocation, ResourceLocation> BOLD_MAP = new HashMap<>();

    @Inject(method = "fromJson", at = @At("HEAD"))
    private static void appendVariantKeys(JsonObject json, CallbackInfoReturnable<BitmapProvider.Builder> cir){
        var src = decompose(GsonHelper.getAsString(json, "src"));
        if (json.has("bold"))
            BOLD_MAP.put(src, decompose(GsonHelper.getAsString(json, "bold")));
    }

    @Override
    public @NotNull ResourceLocation getVariedFontLocation(Style style) {
        var original = style.getFont();
        if (style.isBold() && BOLD_MAP.containsKey(original))
            return BOLD_MAP.get(original);
        return Style.DEFAULT_FONT;
    }

    private static @Nullable ResourceLocation decompose(@NotNull String src){
        var separator = src.indexOf(':');
        if (separator != -1)
            return new ResourceLocation(src.substring(0, separator), src.substring(separator + 1));
        return null;
    }
}
