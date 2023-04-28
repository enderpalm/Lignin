package dev.enderpalm.lignin.text.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.enderpalm.lignin.text.container.Badge;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public final class BadgeRenderer{

    public static void render(Badge badge, float x0, float x1, float y0, float y1, float r, float g, float b, float a, Matrix4f poseMatrix, MultiBufferSource bufferSource, int packedLightCoords) {
        int halfSpace = (Badge.TEXT_SPACER_OFFSET << 1) - 1;
        x0 -= halfSpace;
        x1 += halfSpace;
        renderSolidBadge(badge, bufferSource, poseMatrix, x0, y0, x1, y1,a, packedLightCoords);
    }

    private static void renderSolidBadge(Badge badge, MultiBufferSource bufferSource, Matrix4f poseMatrix, float x0, float y0, float x1, float y1, float alpha, int packedLightCoords) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.textIntensityPolygonOffset(new ResourceLocation("textures/misc/white.png")));
        var bg = badge.getBgColor();
        if (bg.isDisabled() || bg.isAuto()) return;
        var bg0 = adjustAlpha(bg.color0(), alpha);
        var bg1 = bg.isGradient() ? adjustAlpha(bg.color1(), alpha) : bg0;
        renderSolidQuad(consumer, poseMatrix, x0, y0, x1, y1, bg0, bg1, packedLightCoords);
    }

    private static void renderSolidQuad(@NotNull VertexConsumer consumer, Matrix4f poseMatrix, float x0, float y0, float x1, float y1, int color0, int color1, int packedLightCoords) {
        consumer.vertex(poseMatrix, x0, y0, 0.0f).color(color0).uv(0, 0).uv2(packedLightCoords).endVertex();
        consumer.vertex(poseMatrix, x0, y1, 0.0f).color(color0).uv(0, 0).uv2(packedLightCoords).endVertex();
        consumer.vertex(poseMatrix, x1, y1, 0.0f).color(color1).uv(0, 0).uv2(packedLightCoords).endVertex();
        consumer.vertex(poseMatrix, x1, y0, 0.0f).color(color1).uv(0, 0).uv2(packedLightCoords).endVertex();
    }

    private static int adjustAlpha(@Nullable Integer raw, float alpha) {
        if (raw == null) return -1;
        int modifiedAlpha = (int) ((float) ((raw >> 24) & 0xFF) * alpha);
        return (raw & 0xFFFFFF) | (modifiedAlpha << 24);
    }

}
