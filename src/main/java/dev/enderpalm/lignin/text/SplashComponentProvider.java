package dev.enderpalm.lignin.text;

import net.minecraft.ChatFormatting;
import net.minecraft.client.User;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SplashComponentProvider extends SplashManager {
    public SplashComponentProvider(User user) {
        super(user);
    }

    public static @NotNull Component getFormattedSplash(){
        return Component.literal("hello world").withStyle(style -> style.withFont(new ResourceLocation("lignin:boxel")).withColor(ChatFormatting.DARK_AQUA));
    }
}
