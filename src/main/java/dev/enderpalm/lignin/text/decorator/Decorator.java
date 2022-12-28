package dev.enderpalm.lignin.text.decorator;

import dev.enderpalm.lignin.text.decorator.enums.DropShadow;
import net.minecraft.network.chat.Component;

public interface Decorator {

    Component addDecoration(Decoration decoration);
    
    Component fillSolid(int color, int shadowColor, DropShadow dropShadow, int borderRadius);

    Decoration getDecoration();
}
