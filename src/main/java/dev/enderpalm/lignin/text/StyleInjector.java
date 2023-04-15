package dev.enderpalm.lignin.text;

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
}
