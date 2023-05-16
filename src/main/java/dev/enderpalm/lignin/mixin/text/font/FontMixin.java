package dev.enderpalm.lignin.mixin.text.font;

import dev.enderpalm.lignin.text.StringRenderOutputAccessor;
import dev.enderpalm.lignin.util.color.OpaqueRGB;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(Font.class)
public abstract class FontMixin {

    @Inject(method = "drawInBatch8xOutline", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Font$StringRenderOutput;<init>(Lnet/minecraft/client/gui/Font;Lnet/minecraft/client/renderer/MultiBufferSource;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/gui/Font$DisplayMode;I)V",
            shift = At.Shift.BY, by = 2, ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void render8xOutlineAsStyle(FormattedCharSequence text, float x, float y, int color, int backgroundColor, Matrix4f matrix, MultiBufferSource bufferSource, int packedLightCoords, CallbackInfo ci, int i, Font.StringRenderOutput stringRenderOutput) {
        ((StringRenderOutputAccessor) stringRenderOutput).setCoord(x, y);
        text.accept((idx, style, code) -> stringRenderOutput.accept(idx, style.withOutline(new OpaqueRGB(i)).withColor(color), code));
        stringRenderOutput.finish(0, x);
        ci.cancel();
    }
}
