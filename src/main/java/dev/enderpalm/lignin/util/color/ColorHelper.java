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

    public static int appendAlphaChannel(int color){
        return ((color & -67108864) == 0) ? color | -16777216 : color;
    }

    public static @Nullable OpaqueRGB applyLuminance(@Nullable OpaqueRGB color, float dimFactor){
       return color != null ? new OpaqueRGB((int) (color.getRed() * dimFactor), (int) (color.getGreen() * dimFactor), (int) (color.getBlue() * dimFactor)) : null;
    }

    public static @Nullable OpaqueRGB hueShift(@Nullable OpaqueRGB color, float intensity){
        if (color == null) return null;
        var result = applyAlphaLuminance(color, 1.0f, 0.55f); // mock
        return result != null ? new OpaqueRGB(result) : null;
    }

    public static @Nullable OpaqueRGB lerpColor(@Nullable OpaqueRGB start, @Nullable OpaqueRGB end, float progress){
        if (start == null || end == null) return null;
        var result = lerpColor(start.getPacked(), end.getPacked(), progress);
        return result != null ? new OpaqueRGB(result) : null;
    }

    public static @Nullable Integer lerpColor(@Nullable Integer start, @Nullable Integer end, float progress){
        if (start == null || end == null) return null;
        progress = Mth.clamp(progress, 0.0f, 1.0f);
        return (int) Mth.lerp(progress, start >>> 24, end >>> 24) << 24
                | (int) Mth.lerp(progress, start >> 16 & 0xff, end >> 16 & 0xff) << 16
                | (int) Mth.lerp(progress, start >> 8 & 0xff, end >> 8 & 0xff) << 8
                | (int) Mth.lerp(progress, start & 0xff, end & 0xff);
    }
}
