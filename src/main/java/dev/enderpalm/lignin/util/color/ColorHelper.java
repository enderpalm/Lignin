package dev.enderpalm.lignin.util.color;

import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public final class ColorHelper {

    public static @Nullable Integer applyAlphaLuminance(@Nullable Integer color, float alpha, float dimFactor) {
        if (color == null) return null;
        else if ((color & -67108864) == 0) color |= -16777216;
        alpha = Mth.clamp(alpha, 0.0f, 1.0f);
        dimFactor = Mth.clamp(dimFactor, 0.0f, 1.0f);
        int newAlpha = (int) ((color >>> 24) * alpha) << 24;
        int dimmedRGB = ((int) ((color >> 16 & 0xff) * dimFactor) << 16) | ((int) ((color >> 8 & 0xff) * dimFactor) << 8) | (int) ((color & 0xff) * dimFactor);
        return newAlpha | dimmedRGB;
    }

    public static @Nullable Integer applyAlphaLuminance(@Nullable OpaqueRGB color, float alpha, float luminance) {
        return applyAlphaLuminance(color != null ? color.getPacked() : null, alpha, luminance);
    }
}
