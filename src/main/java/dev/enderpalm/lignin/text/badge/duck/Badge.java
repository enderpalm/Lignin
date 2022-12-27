package dev.enderpalm.lignin.text.badge.duck;

import net.minecraft.network.chat.Component;

public interface Badge {
    
    Component fillSolid(int color);

    int getColor();
}
