package dev.enderpalm.lignin.text;

import dev.enderpalm.lignin.text.container.Badge;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface StyleInjector {

    default Style duckConstruct(@Nullable Badge badge, @Nullable TextColor textColor, @Nullable Boolean bold, @Nullable Boolean italic, @Nullable Boolean underlined, @Nullable Boolean strikethrough, @Nullable Boolean obfuscated, @Nullable ClickEvent clickEvent, @Nullable HoverEvent hoverEvent, @Nullable String insertion, @Nullable ResourceLocation font){
        return (Style) this;
    }

    default Style withBadge(@Nullable Badge badge){
        return (Style) this;
    }

    default @Nullable Badge getBadge() {
        return null;
    }
}
