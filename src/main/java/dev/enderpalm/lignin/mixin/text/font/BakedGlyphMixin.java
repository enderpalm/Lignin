package dev.enderpalm.lignin.mixin.text.font;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.enderpalm.lignin.text.BakedGlyphAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(BakedGlyph.class)
public abstract class BakedGlyphMixin implements BakedGlyphAccessor {

    @Shadow @Final private float u0;
    @Shadow @Final private float v0;
    @Shadow @Final private float v1;
    @Shadow @Final private float u1;

    @Unique private byte flag;
    private static final float[] foregroundOffset = new float[]{0.0f, 0.01f, 0.01f, 0.02f, 0.02f, 0.02f};

    @Override
    public void setRenderMode(int flag) {
        this.flag = (byte) Mth.clamp(flag, 0, 5);
    }

    @Override
    public boolean isInBadge() {
        return this.flag > 1;
    }

    @Inject(method = "render", at = @At(value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;vertex(Lorg/joml/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void render8xOutline(boolean italic, float x, float y, Matrix4f matrix, VertexConsumer buffer, float red, float green, float blue, float alpha, int packedLight, CallbackInfo ci, int i, float f, float g, float h, float j, float k, float l, float m, float n) {
        var isBadgeShadowOnly = this.flag == 4;
        if ((this.flag & 1) == 0 && !isBadgeShadowOnly) return;
        var depth = foregroundOffset[this.flag];
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0 || (isBadgeShadowOnly && (dx != 1 || dy != 0))) continue;
                buffer.vertex(matrix, f + m + dx, k + dy, depth).color(red, green, blue, alpha).uv(this.u0, this.v0).uv2(packedLight).endVertex();
                buffer.vertex(matrix, f + n + dx, l + dy, depth).color(red, green, blue, alpha).uv(this.u0, this.v1).uv2(packedLight).endVertex();
                buffer.vertex(matrix, g + n + dx, l + dy, depth).color(red, green, blue, alpha).uv(this.u1, this.v1).uv2(packedLight).endVertex();
                buffer.vertex(matrix, g + m + dx, k + dy, depth).color(red, green, blue, alpha).uv(this.u1, this.v0).uv2(packedLight).endVertex();
            }
        }
        ci.cancel();
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;vertex(Lorg/joml/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"), index = 3)
    private float originalGlyphRenderOffset(float x) {
        return x + foregroundOffset[this.flag] + 0.015f;
    }
}
