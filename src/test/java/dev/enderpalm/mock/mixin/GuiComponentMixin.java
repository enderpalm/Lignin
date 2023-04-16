package dev.enderpalm.mock.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.enderpalm.lignin.text.container.Badge;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiComponent.class)
public class GuiComponentMixin {

    @Inject(method = "drawCenteredString(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V",
        at = @At("HEAD"), cancellable = true)
    private static void drawCenterString(PoseStack poseStack, Font font, Component text, int x, int y, int color, CallbackInfo ci){
        Component text1 = text.copy().withStyle(style -> style.withBadge(new Badge(10)).withBadge(new Badge(19)).withBold(true).withStrikethrough(true).withBadge(new Badge(16)));
        FormattedCharSequence formattedCharSequence = text1.getVisualOrderText();
        font.drawShadow(poseStack, formattedCharSequence, (float)(x - font.width(formattedCharSequence) / 2), (float)y, color);
        Component text2 = text.copy().withStyle(style -> style.withBold(true).withBadge(new Badge(14)).withColor(ChatFormatting.RED));
        font.drawShadow(poseStack, text2.getVisualOrderText(), (float)(x - font.width(text2.getVisualOrderText()) / 2), (float)y + 10, color);
        ci.cancel();
    }
}
