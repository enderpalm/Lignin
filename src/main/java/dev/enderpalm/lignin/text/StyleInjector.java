package dev.enderpalm.lignin.text;

import dev.enderpalm.lignin.mixin.text.style.StyleMixin;
import dev.enderpalm.lignin.text.container.Badge;
import dev.enderpalm.lignin.util.color.OpaqueRGB;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public interface StyleInjector {

    default Style withBadge(@Nullable Badge badge){
        return (Style) this;
    }

    default @Nullable Badge getBadge() {
        return null;
    }

    default Style withOutline(@Nullable OpaqueRGB outline) {
        return (Style) this;
    }

    default @Nullable OpaqueRGB getOutline() {
        return null;
    }

    /**
     * <rawB>For internal use only!</rawB>, used by {@link StyleMixin} <br>
     * Used to set badge to newly created {@link Style} instance. <br>
     * @param badge instance of badge to set
     * @param outline instance of outline to set
     */
    @ApiStatus.Internal
    default void innerBindPropertiesToStyle(@Nullable Badge badge, @Nullable OpaqueRGB outline){}
}
