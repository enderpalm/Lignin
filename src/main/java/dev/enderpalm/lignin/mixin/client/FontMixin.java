package dev.enderpalm.lignin.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import dev.enderpalm.lignin.text.decorator.DecoratorRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(Font.class)
public abstract class FontMixin {

    @Shadow public abstract int drawInBatch(Component text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffer, boolean transparent, int backgroundColor, int packedLight);

    @Inject(method = "draw(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/network/chat/Component;FFI)I", at = @At("HEAD"), cancellable = true)
    public void drawComponent(PoseStack poseStack, Component text, float x, float y, int color, @NotNull CallbackInfoReturnable<Integer> cir){
        cir.setReturnValue(this.drawComponentWithPoseStack(poseStack, text, x, y, color, false));
        cir.cancel();
    }

    @Inject(method = "drawShadow(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/network/chat/Component;FFI)I", at = @At("HEAD"), cancellable = true)
    public void drawShadowComponent(PoseStack poseStack, Component text, float x, float y, int color, @NotNull CallbackInfoReturnable<Integer> cir){
        cir.setReturnValue(this.drawComponentWithPoseStack(poseStack, text, x, y, color, true));
        cir.cancel();
    }

    /**
     * Replace old drawInternal method in {@link Font} to accept PoseStack instead of last().pose()
     */
    private int drawComponentWithPoseStack(@NotNull PoseStack poseStack, Component component, float x, float y, int color, boolean drawShadow){
        DecoratorRenderer.decoratePre(poseStack, component, x, y);
        MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        int i = this.drawInBatch(component, x, y, color, drawShadow, poseStack.last().pose(), bufferSource, false, 0, 15728880);
        bufferSource.endBatch();
        DecoratorRenderer.decoratePost(poseStack, component, x, y);
        return i;
    }
}
