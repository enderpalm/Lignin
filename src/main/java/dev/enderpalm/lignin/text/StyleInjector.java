package dev.enderpalm.lignin.text;

import dev.enderpalm.lignin.mixin.text.style.StyleMixin;
import dev.enderpalm.lignin.text.container.Badge;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

public interface StyleInjector {

    default Style withBadge(@Nullable Badge badge){
        return (Style) this;
    }

    default @Nullable Badge getBadge() {
        return null;
    }

    /**
     * <b>For internal use only!</b>, used by {@link StyleMixin} <br>
     * Used to set badge to newly created {@link Style} instance. <br>
     * @param badge instance of {@link Badge} to set
     */
    default void innerSetBadge(@Nullable Badge badge) {
    }
}
