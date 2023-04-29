package dev.enderpalm.lignin.mixin.text;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.enderpalm.lignin.text.render.BadgeRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Reorder and flip render layer of <b>renderNameTag()</b> method.
 * @reason Z-offset is opposite to usual UI components rendering depth (in usual components, greater depth means object will be rendered in front, but in this case, it requires lesser depth to be in front) making badge render on top of text - try to render this thingy with same {@link BadgeRenderer} as other UI components
 */
@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

    @Inject(method = "renderNameTag", at = @At(value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V", shift = At.Shift.AFTER))
    private void flipRenderOrder(T entity, Component displayName, PoseStack matrixStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci){
        // Scaling every axis by -1.0f and rotate 180 deg around Z axis
        Matrix4f transformer = new Matrix4f(1.0f, -0.0f, -0.0f, 0.0f,
                                            -0.0f, 1.0f, -0.0f, 0.0f,
                                            -0.0f, -0.0f, -1.0f, 0.0f,
                                            -0.0f, -0.0f, -0.0f, 1.0f);
        matrixStack.mulPoseMatrix(transformer);
    }
}
