package dev.enderpalm.lignin.text.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.enderpalm.lignin.text.container.Badge;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public final class BadgeRenderer {

    private static final ResourceLocation WHITE_TEXTURE = new ResourceLocation("textures/misc/white.png");

    public static void render(Badge badge, float x0, float x1, float y0, float y1, float depth, int tint, Matrix4f poseMatrix, MultiBufferSource bufferSource, Font.DisplayMode displayMode, int packedLightCoords) {
        renderSolidBadge(badge, bufferSource, poseMatrix, x0, y0, x1, y1, depth, tint, displayMode, packedLightCoords);
    }

    private static void renderSolidBadge(Badge badge, MultiBufferSource bufferSource, Matrix4f poseMatrix, float x0, float y0, float x1, float y1, float depth, int tint, Font.DisplayMode displayMode, int packedLightCoords) {
        VertexConsumer consumer = bufferSource.getBuffer(Font.DisplayMode.SEE_THROUGH.equals(displayMode) ? RenderType.textIntensitySeeThrough(WHITE_TEXTURE) : RenderType.textIntensity(WHITE_TEXTURE));
        var bg = badge.getBgColor();
        if (bg.isDisabled() || bg.isAuto()) return;
        var bg0 = multiply(bg.color0(), tint);
        var bg1 = bg.isGradient() ? multiply(bg.color1(), tint) : bg0;
        renderSolidQuad(consumer, poseMatrix, x0, y0, x1, y1, depth, bg0, bg1, packedLightCoords);
    }

    private static void renderSolidQuad(@NotNull VertexConsumer consumer, Matrix4f poseMatrix, float x0, float y0, float x1, float y1, float depth, int color0, int color1, int packedLightCoords) {
        consumer.vertex(poseMatrix, x0, y0, depth).color(color0).uv(0, 0).uv2(packedLightCoords).endVertex();
        consumer.vertex(poseMatrix, x0, y1, depth).color(color0).uv(0, 0).uv2(packedLightCoords).endVertex();
        consumer.vertex(poseMatrix, x1, y1, depth).color(color1).uv(0, 0).uv2(packedLightCoords).endVertex();
        consumer.vertex(poseMatrix, x1, y0, depth).color(color1).uv(0, 0).uv2(packedLightCoords).endVertex();
    }

    private static int multiply(@Nullable Integer color, int tint) {
        if (color == null) return tint;
        return FastColor.ARGB32.multiply(color, tint);
    }
}
