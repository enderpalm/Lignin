package dev.enderpalm.lignin.text.container;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.enderpalm.lignin.util.StringCollector;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Badge {

    @Nullable private final Integer bg0;
    @Nullable private final Integer bg1;
    @Nullable private final Boolean roundCorner;
    @Nullable private final Integer border0;
    @Nullable private final Integer border1;
    @Nullable private final Integer shadowColor;
    @Nullable private final ResourceLocation texture;

    /**
     * Pixel count between horizontal border and badge content.
     */
    public static final int BADGE_BORDER_SPACER = 2;

    public Badge(@Nullable Integer bg0, @Nullable Integer bg1, @Nullable Boolean roundCorner, @Nullable Integer border0, @Nullable Integer border1, @Nullable Integer shadowColor, @Nullable ResourceLocation texture) {
        this.bg0 = bg0;
        this.bg1 = bg1;
        this.roundCorner = roundCorner;
        this.border0 = border0;
        this.border1 = border1;
        this.shadowColor = shadowColor;
        this.texture = texture;
    }

    public Badge() {
        this(null, null, null, null, null, null, null);
    }

    public Badge withBgColor(@Nullable Integer bgColor) {
        bgColor = ensureAlpha(bgColor);
        if (this.bg0 != null && this.bg1 != null) return this;
        if (this.bg0 == null)
            return new Badge(bgColor, this.bg1, this.roundCorner, this.border0, this.border1, this.shadowColor, this.texture);
        return new Badge(this.bg0, bgColor, this.roundCorner, this.border0, this.border1, this.shadowColor, this.texture);
    }

    public Badge withBgColor(@Nullable String bgColor) {
        return this.withBgColor(parseColor(bgColor));
    }

    public Badge withRoundCorner(@Nullable Boolean roundCorner) {
        return new Badge(this.bg0, this.bg1, roundCorner, this.border0, this.border1, this.shadowColor, this.texture);
    }

    public Badge withBorderColor(@Nullable Integer borderColor) {
        borderColor = ensureAlpha(borderColor);
        if (this.border0 != null && this.border1 != null) return this;
        if (this.border0 == null)
            return new Badge(this.bg0, this.bg1, this.roundCorner, borderColor, this.border1, this.shadowColor, this.texture);
        return new Badge(this.bg0, this.bg1, this.roundCorner, this.border0, borderColor, this.shadowColor, this.texture);
    }

    public Badge withBorderColor(@Nullable String borderColor) {
        return this.withBorderColor(parseColor(borderColor));
    }

    public Badge withShadowColor(@Nullable Integer shadowColor) {
        return new Badge(this.bg0, this.bg1, this.roundCorner, this.border0, this.border1, shadowColor, this.texture);
    }

    public Badge withTexture(@Nullable ResourceLocation texture) {
        return new Badge(this.bg0, this.bg1, this.roundCorner, this.border0, this.border1, this.shadowColor, texture);
    }

    public ColorPair getBgColor() {
        return new ColorPair(this.bg0, this.bg1);
    }

    public ColorPair getBorderColor() {
        return new ColorPair(this.border0, this.border1);
    }

    public @Nullable Boolean hasRoundCorner() {
        return this.roundCorner;
    }

    public @Nullable Integer getShadowColor() {
        return this.shadowColor;
    }

    public @Nullable ResourceLocation getTexture() {
        return this.texture;
    }

    private static @Nullable Integer parseColor(@Nullable String color) {
        if (color != null) {
            var len = color.length();
            if (color.equals("auto")) return -1;
            if (color.startsWith("#")) {
                if (len > 9) throw new JsonSyntaxException("Invalid color: " + color);
                else if (len < 8) return Integer.parseInt(color.substring(1), 16) | 0xFF000000;
                else {
                    var alpha = Integer.parseInt(color.substring(1, len - 6), 16);
                    var rgb = Integer.parseInt(color.substring(len - 6), 16);
                    return (alpha << 24) | rgb;
                }
            } else return Integer.parseInt(color);
        }
        return null;
    }

    public Badge mergeFrom(@Nullable Badge src) {
        if (this.isEmpty()) return src;
        else if (src == null || src.isEmpty() || src.equals(this)) return this;
        return new Badge(this.bg0 == null ? src.bg0 : this.bg0, this.bg1 == null ? src.bg1 : this.bg1, this.roundCorner == null ? src.roundCorner : this.roundCorner, this.border0 == null ? src.border0 : this.border0, this.border1 == null ? src.border1 : this.border1, this.shadowColor == null ? src.shadowColor : this.shadowColor, this.texture == null ? src.texture : this.texture);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Badge badge)) return false;
        return Objects.equals(this.bg0, badge.bg0)
                && Objects.equals(this.bg1, badge.bg1)
                && Objects.equals(this.roundCorner, badge.roundCorner)
                && Objects.equals(this.border0, badge.border0)
                && Objects.equals(this.border1, badge.border1)
                && Objects.equals(this.shadowColor, badge.shadowColor)
                && Objects.equals(this.texture, badge.texture);
    }

    public boolean isEmpty() {
        return this.bg0 == null && this.bg1 == null && this.roundCorner == null && this.border0 == null && this.border1 == null && this.shadowColor == null && this.texture == null;
    }

    @Override
    public String toString() {
        StringCollector collector = new StringCollector();
        collector.collect("bg0", this.bg0)
                .collect("bg1", this.bg1)
                .collect("roundCorner", this.roundCorner)
                .collect("border0", this.border0)
                .collect("border1", this.border1)
                .collect("shadowColor", this.shadowColor)
                .collect("texture", this.texture);
        return collector.terminate().toString();
    }

    public static JsonObject serialize(@Nullable Badge badge) {
        JsonObject json = new JsonObject();
        if (badge != null) {
            if (badge.bg0 != null) json.addProperty("bg0", badge.bg0);
            if (badge.bg1 != null) json.addProperty("bg1", badge.bg1);
            if (badge.roundCorner != null) json.addProperty("roundCorner", badge.roundCorner);
            if (badge.border0 != null) json.addProperty("border0", badge.border0);
            if (badge.border1 != null) json.addProperty("border1", badge.border1);
            if (badge.shadowColor != null) json.addProperty("shadowColor", badge.shadowColor);
            if (badge.texture != null) json.addProperty("texture", badge.texture.toString());
        }
        return json;
    }

    @Nullable
    public static Badge deserialize(@NotNull JsonObject json) {
        if (json.has("badge")) {
            JsonObject obj = GsonHelper.getAsJsonObject(json, "badge");
            Integer bg0 = getColor(obj, "bg0");
            Integer bg1 = getColor(obj, "bg1");
            Boolean roundCorner = obj.has("roundCorner") ? GsonHelper.getAsBoolean(obj, "roundCorner") : null;
            Integer border0 = getColor(obj, "border0");
            Integer border1 = getColor(obj, "border1");
            Integer shadowColor = getColor(obj, "shadowColor");
            ResourceLocation texture = getTexture(obj);
            return new Badge(bg0, bg1, roundCorner, border0, border1, shadowColor, texture);
        }
        return null;
    }

    @Nullable
    private static Integer getColor(JsonObject json, String key) {
        return json.has(key) ? ensureAlpha(parseColor(GsonHelper.getAsString(json, key))) : null;
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

    private static @Nullable Integer ensureAlpha(@Nullable Integer color) {
        if (color == null) return null;
        return (color & -67108864) == 0 ? color | -16777216 : color;
    }

    /**
     * Append render offset between text-badge (entering), badge-text (leaving) and badge-badge (different)
     *
     * @param prev         previous {@link Badge}
     * @param cur          current {@link Badge}
     * @param notLineStart check if the badge is at line start, decrease offset by 1 if false
     * @return Offset amount
     */
    public static int renderOffset(@Nullable Badge prev, @Nullable Badge cur, boolean notLineStart) {
        var lineOffset = notLineStart ? 1 : 0;
        if (prev != null && cur != null && !prev.equals(cur)) // different
            return (BADGE_BORDER_SPACER << 1) + lineOffset;
        else if ((prev == null && cur != null) || (prev != null && cur == null)) // entering - leaving
            return BADGE_BORDER_SPACER + lineOffset;
        return 0;
    }

    public record ColorPair(@Nullable Integer color0, @Nullable Integer color1) {

        public boolean isGradient() {
            return !isDisabled() && !(color1 == null || Objects.equals(color0, color1));
        }

        public boolean isDisabled() {
            return color0 == null;
        }

        public boolean isAuto() {
            return (color0 != null && color0 == -1) || (color1 != null && color1 == -1);
        }
    }

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
