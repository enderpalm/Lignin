package dev.enderpalm.lignin.text.decorator;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class DecoratorRenderer {

    public static void decoratePre(@NotNull PoseStack poseStack, Component component, float x, float y){

    }

    public static void decoratePost(PoseStack poseStack, Component component, float x, float y){

    }
}
