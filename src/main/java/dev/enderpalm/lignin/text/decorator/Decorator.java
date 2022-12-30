package dev.enderpalm.lignin.text.decorator;

import net.minecraft.network.chat.Component;

public interface Decorator {

    default Component addDecoration(Decoration decoration){
        return Component.empty();
    }

    default Decoration getDecoration(){
        return Decoration.DEFAULT;
    }
}
