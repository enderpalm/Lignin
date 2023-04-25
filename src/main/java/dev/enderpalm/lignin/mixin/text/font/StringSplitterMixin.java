package dev.enderpalm.lignin.mixin.text.font;

import dev.enderpalm.lignin.text.container.Badge;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.StringSplitter;
import net.minecraft.util.FormattedCharSequence;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Environment(EnvType.CLIENT)
@Mixin(StringSplitter.class)
public class StringSplitterMixin {

    @Shadow @Final StringSplitter.WidthProvider widthProvider;

    private Badge before;

    @Inject(method = "stringWidth(Lnet/minecraft/util/FormattedCharSequence;)F", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/FormattedCharSequence;accept(Lnet/minecraft/util/FormattedCharSink;)Z"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void appendGraphicWidth(FormattedCharSequence content, CallbackInfoReturnable<Float> cir, MutableFloat mutableFloat){
        before = null;
        content.accept((i, style, j) -> {
            mutableFloat.add(this.widthProvider.getWidth(j, style));
            mutableFloat.add(Badge.renderOffset(before, style.getBadge()));
            before = style.getBadge();
            return true;
        });
        cir.setReturnValue(mutableFloat.floatValue());
    }
}
