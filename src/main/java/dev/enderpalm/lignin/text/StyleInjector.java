package dev.enderpalm.lignin.text;

import dev.enderpalm.lignin.mixin.text.style.StyleMixin;
import dev.enderpalm.lignin.text.container.Badge;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public interface StyleInjector {

    default Style withBadge(@Nullable Badge badge){
        return (Style) this;
    }

    default @Nullable Badge getBadge() {
        return null;
    }

    /**
     * <rawB>For internal use only!</rawB>, used by {@link StyleMixin} <br>
     * Used to set badge to newly created {@link Style} instance. <br>
     * @param badge instance of {@link Badge} to set
     */
    @ApiStatus.Internal
    default void innerSetBadge(@Nullable Badge badge) {
    }

    default Style withOutline(@Nullable TextColor outline) {
        return (Style) this;
    }

    default @Nullable TextColor getOutline() {
        return null;
    }

    /**
     * <rawB>For internal use only!</rawB>, used by {@link StyleMixin} <br>
     * Used to set badge to newly created {@link Style} instance. <br>
     * @param outline instance of {@link TextColor} to set
     */
    @ApiStatus.Internal
    default void innerSetOutline(@Nullable TextColor outline) {
    }
}
