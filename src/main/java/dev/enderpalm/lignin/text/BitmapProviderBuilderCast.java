package dev.enderpalm.lignin.text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public interface BitmapProviderBuilderCast {

    default @NotNull ResourceLocation getVariedFontLocation(Style style){
        return style.getFont();
    }
}
