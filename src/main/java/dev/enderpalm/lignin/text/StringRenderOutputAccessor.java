package dev.enderpalm.lignin.text;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface StringRenderOutputAccessor {

    default void setCoord(float x, float y){}
}
