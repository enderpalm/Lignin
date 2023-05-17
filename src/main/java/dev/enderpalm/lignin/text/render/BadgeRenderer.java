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

@Environment(EnvType.CLIENT)
public final class BadgeRenderer {

    /**
     * Pixel count between horizontal border and badge content.
     */
    public static final int BADGE_BORDER_SPACER = 2;
    private static final ResourceLocation WHITE_TEXTURE = new ResourceLocation("textures/misc/white.png");

    public static void render(Badge badge, float x0, float x1, float y0, float y1, float depth, float alpha, float dim, Matrix4f poseMatrix, MultiBufferSource bufferSource, Font.DisplayMode displayMode, int packedLightCoords) {
        if (!shouldRender(badge)) return;
        renderSolidBadge(badge, bufferSource, poseMatrix, x0, y0, x1, y1, depth, alpha, dim, displayMode, packedLightCoords);
    }

    private static void renderSolidBadge(@NotNull Badge badge, @NotNull MultiBufferSource bufferSource, Matrix4f poseMatrix, float x0, float y0, float x1, float y1, float depth, float alpha, float dim, Font.DisplayMode displayMode, int packedLightCoords) {
        VertexConsumer consumer = bufferSource.getBuffer(Font.DisplayMode.SEE_THROUGH.equals(displayMode) ? RenderType.textIntensitySeeThrough(WHITE_TEXTURE) : RenderType.textIntensity(WHITE_TEXTURE));
        var bg0 = ColorHelper.applyAlphaLuminance(badge.bg0(), alpha, dim);
        var bg1 = ColorHelper.applyAlphaLuminance(badge.bg1(), alpha, dim);
        assert bg0 != null;
        renderSolidQuad(consumer, poseMatrix, x0, y0, x1, y1, depth, bg0, bg1 == null ? bg0 : bg1, packedLightCoords);
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
        return badge != null && !badge.isEmpty() && (badge.bg0() != null || badge.border0() != null);
    }
}
