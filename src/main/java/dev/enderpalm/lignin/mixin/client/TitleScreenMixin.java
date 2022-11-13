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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    private Component formattedSplash;

    @Shadow @Nullable private String splash;

    protected TitleScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At("HEAD"))
    public void getFormattedSplash(CallbackInfo ci) {
        this.formattedSplash = SplashComponentProvider.getFormattedSplash();
        this.splash = formattedSplash.getString();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/TitleScreen;drawCenteredString(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"))
    public void redirectSplashTextRender(@NotNull PoseStack poseStack, Font font, String s, int x, int y, int color){
        drawCenteredString(poseStack, font, this.formattedSplash, x, y, 16777215);
    }
}
