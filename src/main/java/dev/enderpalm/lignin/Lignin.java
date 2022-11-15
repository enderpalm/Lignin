package dev.enderpalm.lignin;

import dev.enderpalm.lignin.text.SplashComponentProvider;
import dev.enderpalm.lignin.util.TimeHelper;
import net.fabricmc.api.ModInitializer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class Lignin implements ModInitializer {

    public static HashMap<TimeHelper.MonthAndDay, Component> SPLASH_EVENT_MAP = new HashMap<>();

    @Override
    public void onInitialize() {
        new SplashComponentProvider().append(new TimeHelper.MonthAndDay(11,16),Component.literal("1116").withStyle(style -> style.withFont(new ResourceLocation("lignin:boxel"))));
    }
}
