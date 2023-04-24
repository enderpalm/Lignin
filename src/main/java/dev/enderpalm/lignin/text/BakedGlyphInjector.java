package dev.enderpalm.lignin.text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface BakedGlyphInjector {

    default void accept(int idx, Style style, int codePoint) {
    }

    default int getIdx() {
        return 0;
    }

    default @Nullable Style getStyle() {
        return null;
    }

    default int getCodePoint() {
        return 0;
    }
}
