package dev.enderpalm.mock.mixin;

import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Inject(method = "init()V", at = @At("HEAD"))
    public void inject(CallbackInfo ci){
        System.out.println("TitleScreenMixin.inject()2");
    }
}
