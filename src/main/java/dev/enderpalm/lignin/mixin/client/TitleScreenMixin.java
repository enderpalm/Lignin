package dev.enderpalm.lignin.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.enderpalm.lignin.text.SplashComponentProvider;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    @Shadow @Nullable private String splash;

    protected TitleScreenMixin(Component component) {
        super(component);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/TitleScreen;drawCenteredString(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"))
    public void redirectSplashTextRender(@NotNull PoseStack poseStack, Font font, String s, int x, int y, int color){
        SplashComponentProvider provider = new SplashComponentProvider();
        Component formattedSplash = provider.getFormattedSplash();
        if (!provider.forceShowVanilla()) drawCenteredString(poseStack, font, formattedSplash, x, y,16777215);
        else drawCenteredString(poseStack, font, this.splash, x, y,16776960);
    }
}
