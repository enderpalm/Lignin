package dev.enderpalm.lignin.mixin.text.font;

import dev.enderpalm.lignin.text.BakedGlyphAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(BakedGlyph.class)
public abstract class BakedGlyphMixin implements BakedGlyphAccessor {

    /**
     * 0: normal <br>
     * 1: badge content <br>
     * 2: badge content's shadow
     */
    private byte flag;

    @Override
    public void setRenderOffsetMode(int flag) {
        this.flag = (byte) flag;
    }

    @Override
    public boolean isNotInBadge() {
        return this.flag != 1 && this.flag != 2;
    }

    @ModifyArg(method = {"render", "renderEffect"}, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;vertex(Lorg/joml/Matrix4f;FFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"), index = 3)
    private float render(float x) {
        var f = this.flag;
        return x + (f == 0 ? 0 : f == 1 ? 0.08f : 0.01f);
    }
}
