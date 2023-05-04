package dev.enderpalm.lignin.mixin.text.font;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.enderpalm.lignin.text.BakedGlyphCast;
import dev.enderpalm.lignin.text.container.Badge;
import dev.enderpalm.lignin.text.render.BadgeRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;

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
    @Shadow @Final private Font.DisplayMode mode;

    @Nullable private Badge prevBadge;
    private float x0;
    private boolean isNotLineStart;
    private ArrayList<Badge.BadgeBuffer> badgeBuffer = new ArrayList<>();

    @Inject(method = "accept", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/chat/Style;isBold()Z", shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void accept(int i, Style style, int j, CallbackInfoReturnable<Boolean> cir, FontSet fontSet, GlyphInfo glyphInfo, BakedGlyph bakedGlyph) {
        ((BakedGlyphCast) bakedGlyph).setNotInBadge(style);
        Badge currentBadge = style.getBadge();
        var offset = Badge.renderOffset(this.prevBadge, currentBadge, this.isNotLineStart);
        this.x += offset;
        if (currentBadge != null) {
            if (offset != 0) {
                this.x0 = this.x - Badge.BADGE_BORDER_SPACER;
                this.badgeBuffer.add(new Badge.BadgeBuffer(currentBadge, this.x0));
            }
            this.badgeBuffer.get(this.badgeBuffer.size() - 1).setX1(this.x + (style.isBold() ? 1 : 0));
        }
        this.isNotLineStart = true;
        this.prevBadge = style.getBadge();
    }

    @Inject(method = "accept", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;",
            shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void renderBadgeContentShadow(int i, Style style, int j, CallbackInfoReturnable<Boolean> cir, FontSet fontSet, GlyphInfo glyphInfo, BakedGlyph bakedGlyph, boolean bl, float g, float h, float l, float f, TextColor textColor, float m, float n) {
        if (((BakedGlyphCast) bakedGlyph).isNotInBadge() || this.dropShadow) return;
        assert style.getBadge() != null;
        var shadowColor = style.getBadge().getShadowColor();
        if (shadowColor != null){
            VertexConsumer fakeConsumer = this.bufferSource.getBuffer(bakedGlyph.renderType(this.mode));
            bakedGlyph.render(style.isItalic(), this.x + m + 1.0f, this.y, this.pose, fakeConsumer, FastColor.ARGB32.red(shadowColor) / 255.0f * this.r, FastColor.ARGB32.green(shadowColor) / 255.0f * this.g, FastColor.ARGB32.blue(shadowColor) / 255.0f * this.b ,FastColor.ARGB32.alpha(shadowColor) / 255.0f * this.a, this.packedLightCoords);
        }
    }

    @Redirect(method = "accept", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Font;renderChar(Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;ZZFFFLorg/joml/Matrix4f;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFFFI)V"))
    private void disableBadgeTextShadow(Font instance, BakedGlyph glyph, boolean bold, boolean italic, float boldOffset, float x, float y, Matrix4f matrix, VertexConsumer buffer, float red, float green, float blue, float alpha, int packedLight) {
        if (!this.dropShadow || ((BakedGlyphCast) glyph).isNotInBadge())
            instance.renderChar(glyph, bold, italic, boldOffset, x, y, matrix, buffer, red, green, blue, alpha, packedLight);
    }

    @Inject(method = "finish", at = @At("HEAD"))
    private void finish(int backgroundColor, float x, CallbackInfoReturnable<Float> cir) {
        var depth = this.dropShadow ? 0.0f : 0.01f;
        var shadowOffset = this.dropShadow ? 1.0f : 0.0f;
        for (var buffer : this.badgeBuffer)
            BadgeRenderer.render(buffer.getBadge(), buffer.getX0() + shadowOffset, buffer.getX1() + (Badge.BADGE_BORDER_SPACER << 2) - 1 + shadowOffset, this.y - 1 + shadowOffset, this.y + 8 + shadowOffset, depth, FastColor.ARGB32.color((int) (this.a * 255), (int) (this.r * 255), (int) (this.g * 255), (int) (this.b * 255)), this.pose, this.bufferSource, this.mode, this.packedLightCoords);
        this.badgeBuffer = null;
    }
}
