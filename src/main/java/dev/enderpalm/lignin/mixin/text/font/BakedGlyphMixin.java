package dev.enderpalm.lignin.mixin.text.font;

import dev.enderpalm.lignin.text.BakedGlyphInjector;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(BakedGlyph.class)
public class BakedGlyphMixin implements BakedGlyphInjector {

    private int idx = 0;
    private int codePoint = 0;
    @Nullable Style style;

    @Override
    public void accept(int idx, Style style, int codePoint) {
        this.idx = idx;
        this.style = style;
        this.codePoint = codePoint;
    }

    @Override
    public int getIdx() {
        return this.idx;
    }

    @Override
    public @Nullable Style getStyle() {
        return this.style;
    }

    @Override
    public int getCodePoint() {
        return this.codePoint;
    }
}
