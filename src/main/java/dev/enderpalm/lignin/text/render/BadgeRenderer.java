package dev.enderpalm.lignin.text.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.enderpalm.lignin.text.container.Badge;
import dev.enderpalm.lignin.util.color.ColorHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public final class BadgeRenderer {

    /**
     * Pixel count between horizontal border and badge content.
     */
    public static final int BADGE_BORDER_SPACER = 2;
    private static final float HUE_SHIFT_INTENSITY = -0.3f;
    private static final ResourceLocation WHITE_TEXTURE = new ResourceLocation("textures/misc/white.png");

    public static void render(Badge badge, float x0, float x1, float y0, float y1, float depth, float alpha, float dim, Matrix4f poseMatrix, MultiBufferSource bufferSource, Font.DisplayMode displayMode, int packedLightCoords) {
        if (!shouldRender(badge))
            return;
        var bg0 = badge.bg0();
        var bg1 = badge.bg1() != null ? badge.bg1() : bg0;
        var border0 = badge.border0() != null ? badge.border0() : (badge.isAuto(0) ? ColorHelper.hueShift(bg0, HUE_SHIFT_INTENSITY) : null);
        var border1 = badge.border1() != null ? badge.border1() : (badge.isAuto(1) ? ColorHelper.hueShift(bg1, HUE_SHIFT_INTENSITY) : border0);
        var texture = badge.texture();
        if (texture == null)
            renderSolidBadge(ColorHelper.applyAlphaLuminance(bg0, alpha, dim),
                    ColorHelper.applyAlphaLuminance(bg1, alpha, dim),
                    ColorHelper.applyAlphaLuminance(border0, alpha, dim),
                    ColorHelper.applyAlphaLuminance(border1, alpha, dim),
                    badge.roundCorner(), badge.autoMask(), bufferSource, poseMatrix, x0, y0, x1, y1, depth, displayMode, packedLightCoords);
    }

    private static void renderSolidBadge(@Nullable Integer bg0, @Nullable Integer bg1, @Nullable Integer border0, @Nullable Integer border1, boolean roundCorner, byte autoMask, @NotNull MultiBufferSource bufferSource, Matrix4f poseMatrix, float x0, float y0, float x1, float y1, float depth, Font.DisplayMode displayMode, int packedLightCoords) {
        VertexConsumer consumer = bufferSource.getBuffer(Font.DisplayMode.SEE_THROUGH.equals(displayMode) ? RenderType.textIntensitySeeThrough(WHITE_TEXTURE) : RenderType.textIntensity(WHITE_TEXTURE));
        assert bg0 == null || bg1 != null;
        assert border0 == null || border1 != null;
        var inverseWidth = Float.intBitsToFloat(0x7f000000 - Float.floatToIntBits(x1 - x0)); // fast reciprocal (1/x)
        var hasBorder = border0 != null || (autoMask & 1) != 0;
        if (bg0 != null) { // render center section or middle section (roundCorner)
            var shrinkToCenter = hasBorder || roundCorner; // check if badge should be rendered in simple rectangular shape
            var shrinkOffset = shrinkToCenter ? 1 : 0;
            var roundCornerReAdd = (!hasBorder && roundCorner) ? 1 : 0;
            var bgConA = ColorHelper.lerpColor(bg0, bg1, inverseWidth);
            var bgConB = ColorHelper.lerpColor(bg0, bg1, x1 - x0 - inverseWidth);
            renderSolidQuad(consumer, poseMatrix, x0 + shrinkOffset, y0 + shrinkOffset - roundCornerReAdd, x1 - shrinkOffset, y1 - shrinkOffset + roundCornerReAdd, depth,
                    shrinkToCenter ? Objects.requireNonNull(bgConA) : bg0,
                    shrinkToCenter ? Objects.requireNonNull(bgConB) : bg1, packedLightCoords);

            if (roundCorner && !hasBorder) { // render left and right rim (in case of roundCorner with no border)
                renderSolidQuad(consumer, poseMatrix, x0, y0 + 1, x0 + 1, y1 - 1, depth, bg0, bgConA, packedLightCoords);
                renderSolidQuad(consumer, poseMatrix, x1 - 1, y0 + 1, x1, y1 - 1, depth, bgConB, bg1, packedLightCoords);
            }
        }
        if (border0 != null) {
            var borderConA = ColorHelper.lerpColor(border0, border1, inverseWidth);
            var borderConB = ColorHelper.lerpColor(border0, border1, x1 - x0 - inverseWidth);
            var roundCornerRemove = roundCorner ? 1 : 0;
            renderSolidQuad(consumer, poseMatrix, x0, y0 + roundCornerRemove, x0 + 1, y1 - roundCornerRemove, depth, border0, Objects.requireNonNull(borderConA), packedLightCoords);
            renderSolidQuad(consumer, poseMatrix, x1 - 1, y0 + roundCornerRemove, x1, y1 - roundCornerRemove, depth, Objects.requireNonNull(borderConB), border1, packedLightCoords);
            renderSolidQuad(consumer, poseMatrix, x0 + 1, y0, x1 - 1, y0 + 1, depth, borderConA, borderConB, packedLightCoords);
            renderSolidQuad(consumer, poseMatrix, x0 + 1, y1 - 1, x1 - 1, y1, depth, borderConA, borderConB, packedLightCoords);
        }
    }

    private static void renderSolidQuad(@NotNull VertexConsumer consumer, Matrix4f poseMatrix, float x0, float y0, float x1, float y1, float depth, int color0, int color1, int packedLightCoords) {
        consumer.vertex(poseMatrix, x0, y0, depth).color(color0).uv(0, 0).uv2(packedLightCoords).endVertex();
        consumer.vertex(poseMatrix, x0, y1, depth).color(color0).uv(0, 0).uv2(packedLightCoords).endVertex();
        consumer.vertex(poseMatrix, x1, y1, depth).color(color1).uv(0, 0).uv2(packedLightCoords).endVertex();
        consumer.vertex(poseMatrix, x1, y0, depth).color(color1).uv(0, 0).uv2(packedLightCoords).endVertex();
    }

    /**
     * Append render offset between text-badge (entering), badge-text (leaving) and badge-badge (different)
     *
     * @param prev         previous {@link Badge}
     * @param cur          current {@link Badge}
     * @param notLineStart check if the badge is at line start, decrease offset by 1 if false
     * @return Offset amount
     */
    public static int renderOffset(@Nullable Badge prev, @Nullable Badge cur, boolean notLineStart) {
        var lineOffset = notLineStart ? 1 : 0;
        var prevResult = shouldRender(prev);
        var curResult = shouldRender(cur);
        if (prevResult && curResult && !prev.equals(cur)) // different
            return (BADGE_BORDER_SPACER << 1) + lineOffset; // 1st offset + spacer(1px) + 2nd offset
        else if ((!prevResult && curResult) || (prevResult && !curResult)) // entering - leaving
            return BADGE_BORDER_SPACER + lineOffset;
        return 0;
    }

    public static boolean shouldRender(@Nullable Badge badge) {
        return badge != null && !badge.isEmpty() && (badge.bg0() != null || badge.border0() != null || badge.isAuto(0));
    }
}
