package dev.enderpalm.lignin.mixin.text.font;

import dev.enderpalm.lignin.text.BakedGlyphCast;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(BakedGlyph.class)
public abstract class BakedGlyphMixin implements BakedGlyphCast {

    private boolean isNotInBadge;

    @Override
    public void setNotInBadge(Style style) {
        this.isNotInBadge = style == null || style.getBadge() == null;
    }

    @Override
    public boolean isNotInBadge() {
        return this.isNotInBadge;
    }

    @ModifyArg(method = {"render", "renderEffect"}, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;vertex(Lorg/joml/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"), index = 3)
    private float render(float x) {
        return x + 0.02f;
    }
}
