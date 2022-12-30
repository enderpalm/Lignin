package dev.enderpalm.lignin.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GuiComponent.class)
public abstract class GuiComponentMixin {

    @Inject(method = "drawCenteredString(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V",
            at = @At("HEAD"), cancellable = true)
    private static void drawCenteredString(PoseStack poseStack, @NotNull Font font, Component text, int x, int y, int color, @NotNull CallbackInfo ci) {
        font.drawShadow(poseStack, text, x - font.width(text.getVisualOrderText()) / 2.0f, y, color);
        ci.cancel();
    }
}
