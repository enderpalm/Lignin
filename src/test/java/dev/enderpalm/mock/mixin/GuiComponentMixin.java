package dev.enderpalm.mock.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.enderpalm.lignin.text.container.Badge;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiComponent.class)
public class GuiComponentMixin {

    @Inject(method = "drawCenteredString(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V",
        at = @At("HEAD"), cancellable = true)
    private static void drawCenterString(PoseStack poseStack, Font font, Component text, int x, int y, int color, CallbackInfo ci){
        var test = true;
        Component text1 = test ? Component.empty().append(Component.literal("Hello").withStyle(style -> style.withItalic(true).withBadge(new Badge().withCornerRadius(5)))).append(text) : text;
        FormattedCharSequence formattedCharSequence = text1.getVisualOrderText();
        font.drawShadow(poseStack, formattedCharSequence, (float)(x - font.width(formattedCharSequence) / 2), (float)y, color);
        ci.cancel();
    }
}
