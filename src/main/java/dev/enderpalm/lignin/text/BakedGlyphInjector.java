package dev.enderpalm.lignin.text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Style;

@Environment(EnvType.CLIENT)
public interface BakedGlyphInjector {

    default void setInBadge(Style style){

    }

    default boolean isInBadge(){
        return false;
    }
}
