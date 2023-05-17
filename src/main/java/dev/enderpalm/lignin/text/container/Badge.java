package dev.enderpalm.lignin.text.container;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.enderpalm.lignin.util.StringCollector;
import dev.enderpalm.lignin.util.color.OpaqueRGB;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("unused")
public record Badge(@Nullable OpaqueRGB bg0, @Nullable OpaqueRGB bg1, boolean roundCorner, @Nullable OpaqueRGB border0,
                    @Nullable OpaqueRGB border1, @Nullable OpaqueRGB shadowColor, @Nullable ResourceLocation texture,
                    byte autoMask) {

    private static final String AUTO_KEYWORD = "auto";

    public Badge() {
        this(null, null, false, null, null, null, null, (byte) 0);
    }

    public Badge(@Nullable OpaqueRGB bg0, @Nullable OpaqueRGB bg1, boolean roundCorner, @Nullable OpaqueRGB border0, @Nullable OpaqueRGB border1, @Nullable OpaqueRGB shadowColor, @Nullable ResourceLocation texture) {
        this(bg0, bg1, roundCorner, border0, border1, shadowColor, texture, (byte) 0);
    }

    public Badge(Badge badge) {
        this(badge.bg0, badge.bg1, badge.roundCorner, badge.border0, badge.border1, badge.shadowColor, badge.texture, badge.autoMask);
    }

    public Badge withBgColor(@Nullable OpaqueRGB bgColor) {
        if (this.bg0 != null && this.bg1 != null) return this;
        if (this.bg0 == null)
            return new Badge(bgColor, this.bg1, this.roundCorner, this.border0, this.border1, this.shadowColor, this.texture, this.autoMask);
        return new Badge(this.bg0, bgColor, this.roundCorner, this.border0, this.border1, this.shadowColor, this.texture, this.autoMask);
    }

    public Badge withBgColor(int bgColor) {
        return this.withBgColor(new OpaqueRGB(bgColor));
    }

    public Badge withRoundCorner() {
        return new Badge(this.bg0, this.bg1, true, this.border0, this.border1, this.shadowColor, this.texture, this.autoMask);
    }

    public Badge withBorderColor(@Nullable OpaqueRGB borderColor) {
        if (this.border0 != null && this.border1 != null) return this;
        if (this.border0 == null)
            return new Badge(this.bg0, this.bg1, this.roundCorner, borderColor, this.border1, this.shadowColor, this.texture, this.autoMask);
        return new Badge(this.bg0, this.bg1, this.roundCorner, this.border0, borderColor, this.shadowColor, this.texture, this.autoMask);
    }

    public Badge withBorderColor(int borderColor) {
        return this.withBorderColor(new OpaqueRGB(borderColor));
    }

    public Badge withAutoBorderColor() {
        var newMask = this.autoMask | ((this.autoMask & 1) + 1);
        return new Badge(this.bg0, this.bg1, this.roundCorner, this.border0, this.border1, this.shadowColor, this.texture, (byte) newMask);
    }

    public Badge withShadowColor(@Nullable OpaqueRGB shadowColor) {
        return new Badge(this.bg0, this.bg1, this.roundCorner, this.border0, this.border1, shadowColor, this.texture, this.autoMask);
    }

    public Badge withShadowColor(int shadowColor) {
        return this.withShadowColor(new OpaqueRGB(shadowColor));
    }

    public Badge withAutoShadowColor() {
        return new Badge(this.bg0, this.bg1, this.roundCorner, this.border0, this.border1, this.shadowColor, this.texture, (byte) (this.autoMask | 4));
    }

    public Badge withTexture(@Nullable ResourceLocation texture) {
        return new Badge(this.bg0, this.bg1, this.roundCorner, this.border0, this.border1, this.shadowColor, texture, this.autoMask);
    }

    public Badge mergeFrom(@Nullable Badge src) {
        if (this.isEmpty()) return src;
        else if (src == null || src.isEmpty() || src.equals(this)) return this;
        return new Badge(this.bg0 == null ? src.bg0 : this.bg0, this.bg1 == null ? src.bg1 : this.bg1, this.roundCorner || src.roundCorner, this.border0 == null ? src.border0 : this.border0, this.border1 == null ? src.border1 : this.border1, this.shadowColor == null ? src.shadowColor : this.shadowColor, this.texture == null ? src.texture : this.texture, this.autoMask);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Badge badge)) return false;
        return Objects.equals(this.bg0, badge.bg0)
                && Objects.equals(this.bg1, badge.bg1)
                && this.roundCorner == badge.roundCorner
                && Objects.equals(this.border0, badge.border0)
                && Objects.equals(this.border1, badge.border1)
                && Objects.equals(this.shadowColor, badge.shadowColor)
                && Objects.equals(this.texture, badge.texture)
                && this.autoMask == badge.autoMask;
    }

    public boolean isEmpty() {
        return this.bg0 == null && this.bg1 == null && !this.roundCorner && this.border0 == null && this.border1 == null && this.shadowColor == null && this.texture == null && this.autoMask == 0;
    }

    public boolean isAuto(int bitOrdinal){
        return (this.autoMask & (1 << bitOrdinal)) != 0;
    }

    @Override
    public String toString() {
        StringCollector collector = new StringCollector();
        collector.collectValue("bg0", this.bg0)
                .collectValue("bg1", this.bg1)
                .collectFlag("roundCorner", this.roundCorner)
                .collectValue("border0", this.border0)
                .collectValue("border1", this.border1)
                .collectValue("shadowColor", this.shadowColor)
                .collectValue("texture", this.texture)
                .collectValue("autoMask", this.autoMask);
        return collector.terminate().toString();
    }

    public static class Serializer{

        public static JsonObject serialize(@Nullable Badge badge) {
            JsonObject json = new JsonObject();
            if (badge != null) {
                if (badge.bg0 != null) json.addProperty("bg0", badge.bg0.toString());
                if (badge.bg1 != null) json.addProperty("bg1", badge.bg1.toString());
                if (badge.roundCorner) json.addProperty("roundCorner", true);
                if (badge.border0 != null) json.addProperty("border0", replaceColorFromMask(badge.border0, badge.autoMask, 0));
                if (badge.border1 != null) json.addProperty("border1", replaceColorFromMask(badge.border1, badge.autoMask, 1));
                if (badge.shadowColor != null) json.addProperty("shadowColor", replaceColorFromMask(badge.shadowColor, badge.autoMask, 2));
                if (badge.texture != null) json.addProperty("texture", badge.texture.toString());
            }
            return json;
        }

        public static @Nullable Badge deserialize(@NotNull JsonObject json) {
            if (json.has("badge")) {
                byte[] autoMask = new byte[]{(byte) 0};
                JsonObject obj = GsonHelper.getAsJsonObject(json, "badge");
                OpaqueRGB bg0 = getColor(obj, "bg0");
                OpaqueRGB bg1 = getColor(obj, "bg1");
                boolean roundCorner = obj.has("roundCorner") && GsonHelper.getAsBoolean(obj, "roundCorner");
                ResourceLocation texture = getTexture(obj);
                OpaqueRGB border0 = getAutoColor(obj, "border0", autoMask, 0, bg0);
                OpaqueRGB border1 = getAutoColor(obj, "border1", autoMask, 1, bg0);
                OpaqueRGB shadowColor = getAutoColor(obj, "shadowColor", autoMask, 2, bg0);
                return new Badge(bg0, bg1, roundCorner, border0, border1, shadowColor, texture, autoMask[0]);
            }
            return null;
        }

        private static @NotNull String replaceColorFromMask(@NotNull OpaqueRGB color, byte mask, int bitOrdinal){
            if ((mask & (1 << bitOrdinal)) != 0)
                return AUTO_KEYWORD;
            else return color.toString();
        }

        private static @Nullable OpaqueRGB getColor(JsonObject json, String key) {
            if (json.has(key)) {
                if (GsonHelper.getAsString(json, key).equals(AUTO_KEYWORD))
                    throw new IllegalArgumentException("Cannot use 'auto' keyword for color: " + key);
                var rawString = GsonHelper.getAsString(json, key);
                return OpaqueRGB.parseColor(rawString);
            }
            return null;
        }

        private static @Nullable OpaqueRGB getAutoColor(JsonObject json, String key, byte[] mask, int bitOrdinal, @Nullable OpaqueRGB bg0) {
            OpaqueRGB color = null;
            if (json.has(key)) {
                if (GsonHelper.getAsString(json, key).equals(AUTO_KEYWORD)) {
                    if (bg0 == null)
                        throw new IllegalArgumentException("Auto color mode for " + key + " requires bg0 to inherit from.");
                    mask[0] |= (1 << bitOrdinal);
                }
                else color = getColor(json, key);
                return color;
            }
            return null;
        }

        @Nullable
        private static ResourceLocation getTexture(JsonObject json) {
            if (json.has("texture")) {
                String location = GsonHelper.getAsString(json, "texture");
                try {
                    return new ResourceLocation(location);
                } catch (ResourceLocationException e) {
                    throw new JsonSyntaxException("Invalid texture location: " + location);
                }
            }
            return null;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class BadgeBuffer {
        @NotNull private final Badge badge;
        private final float x0;
        private float x1;

        public BadgeBuffer(@NotNull Badge badge, float x0) {
            this.badge = badge;
            this.x0 = x0;
            this.x1 = x0;
        }

        public void setX1(float x1) {
            this.x1 = x1;
        }

        public @NotNull Badge getBadge() {
            return this.badge;
        }

        public float getX0() {
            return this.x0;
        }

        public float getX1() {
            return this.x1;
        }
    }
}
