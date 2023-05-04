package dev.enderpalm.lignin.text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Style;

@Environment(EnvType.CLIENT)
public interface BakedGlyphCast {

    default void setNotInBadge(Style style){
    }

    default boolean isNotInBadge(){
        return true;
    }
}
