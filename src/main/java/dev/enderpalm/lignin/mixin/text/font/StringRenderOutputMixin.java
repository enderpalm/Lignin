package dev.enderpalm.lignin.mixin.text.font;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.enderpalm.lignin.text.BakedGlyphAccessor;
import dev.enderpalm.lignin.text.FontEmphases;
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
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;

@Environment(EnvType.CLIENT)
@Mixin(Font.StringRenderOutput.class)
public abstract class StringRenderOutputMixin {

    @Shadow float x;
    @Shadow float y;
    @Shadow @Final private float a;
    @Shadow @Final private Matrix4f pose;
    @Shadow @Final MultiBufferSource bufferSource;
    @Shadow @Final private int packedLightCoords;
    @Shadow @Final private boolean dropShadow;
    @Shadow @Final private Font.DisplayMode mode;

    @Shadow
    protected abstract void addEffect(BakedGlyph.Effect effect);

    @Unique
    @Nullable private Badge prevBadge;

    private boolean isNotLineStart;
    private ArrayList<Badge.BadgeBuffer> badgeBuffer = new ArrayList<>();

    @ModifyVariable(method = "accept", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Style overrideFont(Style style) {
        return FontEmphases.getSwitchedFont(style);
    }

    @Inject(method = "accept", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/chat/Style;isBold()Z", shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void appendBadgeOffset(int i, Style style, int j, CallbackInfoReturnable<Boolean> cir, FontSet fontSet, GlyphInfo glyphInfo, BakedGlyph bakedGlyph) {
        Badge currentBadge = style.getBadge();
        var offset = BadgeRenderer.renderOffset(this.prevBadge, currentBadge, this.isNotLineStart);
        this.x += offset;
        if (currentBadge != null) {
            if (offset != 0)
                this.badgeBuffer.add(new Badge.BadgeBuffer(currentBadge, this.x - BadgeRenderer.BADGE_BORDER_SPACER));
            this.badgeBuffer.get(this.badgeBuffer.size() - 1).setX1(this.x + (style.isBold() ? 1 : 0));
        }
        this.isNotLineStart = true;
        this.prevBadge = style.getBadge();
        RenderSystem.enablePolygonOffset();
        RenderSystem.polygonOffset(-1.0f, -1.0f);
    }

    @Inject(method = "accept", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Font;renderChar(Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;ZZFFFLorg/joml/Matrix4f;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFFFI)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void renderBadgeShadowOrOutline(int i, Style style, int j, CallbackInfoReturnable<Boolean> cir, FontSet fontSet, GlyphInfo glyphInfo, BakedGlyph bakedGlyph, boolean bl, float g, float h, float l, float f, TextColor textColor, float m, float n, VertexConsumer vertexConsumer) {// prefer 1-3-4-5
        var isInBadge = style.getBadge() != null;
        var isBadgeShadow = isInBadge && style.getBadge().shadowColor() != null;
        var foregroundMode = isInBadge ? 2 : 0;
        if (!this.dropShadow && isInBadge && this.mode != Font.DisplayMode.SEE_THROUGH) {
            ((BakedGlyphAccessor) bakedGlyph).setRenderMode(isBadgeShadow ? 3 : 2);
            bakedGlyph.render(bl, this.x + n, this.y + n, this.pose, vertexConsumer, 0.2f, 0.5f, 0.6f, f, this.packedLightCoords);
        }
        ((BakedGlyphAccessor) bakedGlyph).setRenderMode(foregroundMode);
    }

    @Redirect(method = "accept", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Font;renderChar(Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;ZZFFFLorg/joml/Matrix4f;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFFFI)V"))
    private void renderForegroundText(Font instance, BakedGlyph glyph, boolean bold, boolean italic, float boldOffset, float x, float y, Matrix4f matrix, VertexConsumer buffer, float red, float green, float blue, float alpha, int packedLight) {
        var isInBadge = ((BakedGlyphAccessor) glyph).isInBadge();
        if (isInBadge && this.dropShadow) return; // prefer 0-2
        var overlayBuffer = this.bufferSource.getBuffer(glyph.renderType(this.mode == Font.DisplayMode.SEE_THROUGH ? this.mode : Font.DisplayMode.POLYGON_OFFSET));
        instance.renderChar(glyph, bold, italic, boldOffset, x, y, matrix, overlayBuffer, red, green, blue, alpha, packedLight);
    }

    @Redirect(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font$StringRenderOutput;addEffect(Lnet/minecraft/client/gui/font/glyphs/BakedGlyph$Effect;)V"))
    private void disableVanillaEffectAdder(Font.StringRenderOutput instance, BakedGlyph.Effect effect) {
    }

    @Inject(method = "accept", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/chat/Style;isStrikethrough()Z"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void reImplementAddEffect(int i, Style style, int j, CallbackInfoReturnable<Boolean> cir, FontSet fontSet, GlyphInfo glyphInfo, BakedGlyph bakedGlyph, boolean bl, float g, float h, float l, float f, TextColor textColor, float m, float n) {
        var depth = 0.08f;
        if (!this.dropShadow || !((BakedGlyphAccessor) bakedGlyph).isInBadge()) {
            if (style.isStrikethrough()) {
                var offset = FontEmphases.getShiftedEffect(style, false);
                this.addEffect(new BakedGlyph.Effect(this.x + n - offset.thickness(), this.y + n + offset.shift(), this.x + n + m, this.y + n + offset.shift() - offset.thickness(), depth, g, h, l, f));
            }
            if (style.isUnderlined()) {
                var offset = FontEmphases.getShiftedEffect(style, true);
                this.addEffect(new BakedGlyph.Effect(this.x + n - offset.thickness(), this.y + n + offset.shift(), this.x + n + m, this.y + n + offset.shift() - offset.thickness(), depth, g, h, l, f));
            }
        }
    }

    @Inject(method = "finish", at = @At("HEAD"))
    private void invokeBadgeRenderer(int backgroundColor, float x, CallbackInfoReturnable<Float> cir) {
        var depth = 0.0f;
        var badgeDimFactor = this.dropShadow ? 0.45f : 1.0f;
        var shadowOffset = this.dropShadow ? 1.0f : 0.0f;
        for (var buffer : this.badgeBuffer)
            BadgeRenderer.render(buffer.getBadge(), buffer.getX0() + shadowOffset, buffer.getX1() + (BadgeRenderer.BADGE_BORDER_SPACER << 2) - 1 + shadowOffset, this.y - 1 + shadowOffset, this.y + 8 + shadowOffset, depth, this.a, badgeDimFactor, this.pose, this.bufferSource, this.mode, this.packedLightCoords);
        this.badgeBuffer = null;
        RenderSystem.disablePolygonOffset();
    }

    @ModifyArg(method = "finish", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;renderType(Lnet/minecraft/client/gui/Font$DisplayMode;)Lnet/minecraft/client/renderer/RenderType;"),
            index = 0)
    private Font.DisplayMode changeEffectRenderMode(Font.DisplayMode displayMode) {
        return displayMode.equals(Font.DisplayMode.SEE_THROUGH) ? displayMode : Font.DisplayMode.POLYGON_OFFSET;
    }
}
