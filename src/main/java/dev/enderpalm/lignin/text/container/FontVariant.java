package dev.enderpalm.lignin.text.container;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public final class FontVariant {

    public static HashMap<ResourceLocation, ResourceLocation> BOLD_MAP = new HashMap<>();

    public static @NotNull Style getSwitchedFont(@NotNull Style original){
        var originalFont = original.getFont();
        if (original.isBold() && BOLD_MAP.containsKey(originalFont)) {
            if (BOLD_MAP.get(originalFont) == null) return original.withBold(false);
            return original.withFont(BOLD_MAP.get(originalFont)).withBold(false);
        }
        return original;
    }
}
