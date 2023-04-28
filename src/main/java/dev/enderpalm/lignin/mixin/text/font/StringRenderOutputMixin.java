package dev.enderpalm.lignin.mixin.text.font;

import com.mojang.blaze3d.font.GlyphInfo;
import dev.enderpalm.lignin.text.container.Badge;
import dev.enderpalm.lignin.text.render.BadgeRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
@Mixin(Font.StringRenderOutput.class)
public abstract class StringRenderOutputMixin {

    @Shadow float x;
    @Shadow float y;
    @Shadow @Final private float g;
    @Shadow @Final private float r;
    @Shadow @Final private float b;
    @Shadow @Final private float a;
    @Shadow @Final private Matrix4f pose;
    @Shadow @Final MultiBufferSource bufferSource;
    @Shadow @Final private int packedLightCoords;
    @Shadow @Final private boolean dropShadow;

    @Nullable private Badge beforeBadge;

    private float x0;
    private HashMap<Badge.BadgeCoord, Float> badgeCoords = new HashMap<>();

    @Inject(method = "accept", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/chat/Style;isBold()Z", shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void accept(int i, Style style, int j, CallbackInfoReturnable<Boolean> cir, FontSet fontSet, GlyphInfo glyphInfo, BakedGlyph bakedGlyph) {
        bakedGlyph.accept(i, style, j);
        Badge currentBadge = style.getBadge();
        var offset = Badge.renderOffset(this.beforeBadge, currentBadge);
        if (style.getBadge() != null) {
            if (offset != 0) {
                this.x0 = this.x;
                this.badgeCoords.put(new Badge.BadgeCoord(currentBadge, this.x0), this.x0);
            } else this.badgeCoords.put(new Badge.BadgeCoord(currentBadge, this.x0), this.x);
        }
        this.x += offset;
        beforeBadge = style.getBadge();
    }

    @Inject(method = "finish", at = @At("HEAD"))
    private void finish(int backgroundColor, float x, CallbackInfoReturnable<Float> cir) {
        if (!this.dropShadow) {
            int renderOffset = Badge.TEXT_SPACER_OFFSET << 1;
            for (var buffer : this.badgeCoords.entrySet()) {
                BadgeRenderer.render(buffer.getKey().badge(), buffer.getKey().x0() + renderOffset, buffer.getValue() + renderOffset, this.y - 1, this.y + 8, this.r, this.g, this.b, this.a, this.pose, this.bufferSource, this.packedLightCoords);
            }
        }
        this.badgeCoords = null;
    }
}
