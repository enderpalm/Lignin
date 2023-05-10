package dev.enderpalm.lignin.text;

import com.google.gson.JsonObject;
import dev.enderpalm.lignin.Lignin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public final class FontEmphases {

    public static HashMap<ResourceLocation, Emphases> EMPHASES_MAP = new HashMap<>();
    private static final ResourceLocation EMPTY = Lignin.REF.getPath("");

    public static @NotNull Style getSwitchedFont(@NotNull Style original) {
        var originalFont = original.getFont();
        if (EMPHASES_MAP.containsKey(originalFont)) {
            var emphases = EMPHASES_MAP.get(originalFont);
            if (original.isBold() && emphases.bold != null)
                return emphases.bold.equals(EMPTY) ? original.withBold(false) : original.withFont(emphases.bold).withBold(false);
            if (original.isItalic() && emphases.italic != null)
                return emphases.italic.equals(EMPTY) ? original.withItalic(false) : original.withFont(emphases.italic).withItalic(false);
        }
        return original;
    }

    public static @NotNull EffectOffset getShiftedEffect(@NotNull Style original, boolean isUnderlined) {
        var originalFont = original.getFont();
        if (EMPHASES_MAP.containsKey(originalFont)){
            var emphases = EMPHASES_MAP.get(originalFont);
            if (emphases.strikethrough != null && !isUnderlined)
                return emphases.strikethrough;
            if (emphases.underlined != null && isUnderlined)
                return emphases.underlined;
        }
        return new EffectOffset(isUnderlined ? 9.0f : 4.5f, 1.0f);
    }

    public static @Nullable ResourceLocation deserializeLocation(@NotNull JsonObject json, @NotNull String key) {
        if (!json.has(key)) return null;
        var location = GsonHelper.getAsString(json, key);
        var separator = location.indexOf(':');
        if (separator != -1)
            return new ResourceLocation(location.substring(0, separator), location.substring(separator + 1));
        return EMPTY;
    }

    public static @Nullable EffectOffset deserializeEffectOffset(@NotNull JsonObject json, @NotNull String key) {
        if (!json.has(key)) return null;
        var obj = GsonHelper.getAsJsonObject(json, key);
        var shift = GsonHelper.getAsFloat(obj, "shift", key.equals("underlined") ? 9.0f : 4.5f);
        var thickness = GsonHelper.getAsFloat(obj, "thickness", 1.0f);
        return new EffectOffset(shift, thickness);
    }

    public record EffectOffset(float shift, float thickness) {
    }

    public record Emphases(@Nullable ResourceLocation bold, @Nullable ResourceLocation italic,
                           @Nullable EffectOffset strikethrough, @Nullable EffectOffset underlined) {
    }
}
