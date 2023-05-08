package dev.enderpalm.lignin.text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface BakedGlyphAccessor {

    default void setRenderOffsetMode(int flag){
    }

    default boolean isNotInBadge(){
        return false;
    }
}
