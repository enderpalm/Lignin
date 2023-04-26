package dev.enderpalm.lignin.mixin.text.font;

import dev.enderpalm.lignin.text.BakedGlyphInjector;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(BakedGlyph.class)
public abstract class BakedGlyphMixin implements BakedGlyphInjector {

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

    @ModifyArg(method = {"render", "renderEffect"}, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;vertex(Lorg/joml/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"), index = 3)
    private float render(float x){
        return x + 0.01f;
    }
}
