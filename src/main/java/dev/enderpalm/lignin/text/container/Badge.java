package dev.enderpalm.lignin.text.container;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.enderpalm.lignin.text.StringCollector;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import java.util.Objects;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class Badge {

    @Nullable private final Integer bg0;
    @Nullable private final Integer bg1;
    @Nullable private final Integer cornerRadius;
    @Nullable private final Integer border0;
    @Nullable private final Integer border1;
    @Nullable private final Integer shadowColor;
    @Nullable private final ShadowDir shadowDir;
    @Nullable private final ResourceLocation texture;

    /**
     * Pixel count between horizontal border and badge content.
     */
    public static final int BADGE_BORDER_SPACER = 2;

    public Badge(@Nullable Integer bg0, @Nullable Integer bg1, @Nullable Integer cornerRadius, @Nullable Integer border0, @Nullable Integer border1, @Nullable Integer shadowColor, @Nullable ShadowDir shadowDir, @Nullable ResourceLocation texture) {
        this.bg0 = bg0;
        this.bg1 = bg1;
        this.cornerRadius = cornerRadius;
        this.border0 = border0;
        this.border1 = border1;
        this.shadowColor = shadowColor;
        this.shadowDir = shadowDir;
        this.texture = texture;
    }

    public Badge() {
        this(null, null, null, null, null, null, null, null);
    }

    public Badge(@NotNull Badge src) {
        this.bg0 = src.bg0;
        this.bg1 = src.bg1;
        this.cornerRadius = src.cornerRadius;
        this.border0 = src.border0;
        this.border1 = src.border1;
        this.shadowColor = src.shadowColor;
        this.shadowDir = src.shadowDir;
        this.texture = src.texture;
    }

    public Badge withBgColor(@Nullable Integer bgColor) {
        bgColor = ensureAlpha(bgColor);
        if (this.bg0 != null && this.bg1 != null) return this;
        if (this.bg0 == null)
            return new Badge(bgColor, this.bg1, this.cornerRadius, this.border0, this.border1, this.shadowColor, this.shadowDir, this.texture);
        return new Badge(this.bg0, bgColor, this.cornerRadius, this.border0, this.border1, this.shadowColor, this.shadowDir, this.texture);
    }

    public Badge withBgColor(@Nullable String bgColor) {
        return this.withBgColor(parseColor(bgColor));
    }

    public Badge withCornerRadius(@Nullable Integer cornerRadius) {
        return new Badge(this.bg0, this.bg1, cornerRadius, this.border0, this.border1, this.shadowColor, this.shadowDir, this.texture);
    }

    public Badge withBorderColor(@Nullable Integer borderColor) {
        borderColor = ensureAlpha(borderColor);
        if (this.border0 != null && this.border1 != null) return this;
        if (this.border0 == null)
            return new Badge(this.bg0, this.bg1, this.cornerRadius, borderColor, this.border1, this.shadowColor, this.shadowDir, this.texture);
        return new Badge(this.bg0, this.bg1, this.cornerRadius, this.border0, borderColor, this.shadowColor, this.shadowDir, this.texture);
    }

    public Badge withBorderColor(@Nullable String borderColor) {
        return this.withBorderColor(parseColor(borderColor));
    }

    public Badge withTextShadow(@Nullable Integer shadowColor, @Nullable ShadowDir shadowDir) {
        if (shadowColor == null || shadowDir == null) return this;
        return new Badge(this.bg0, this.bg1, this.cornerRadius, this.border0, this.border1, shadowColor, shadowDir, this.texture);
    }

    public Badge withTexture(@Nullable ResourceLocation texture) {
        return new Badge(this.bg0, this.bg1, this.cornerRadius, this.border0, this.border1, this.shadowColor, this.shadowDir, texture);
    }

    public ColorPair getBgColor() {
        return new ColorPair(this.bg0, this.bg1);
    }

    public ColorPair getBorderColor() {
        return new ColorPair(this.border0, this.border1);
    }

    public @Nullable Integer getCornerRadius() {
        return this.cornerRadius;
    }

    public @Nullable ShadowDir getShadowDir() {
        return this.shadowDir;
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
        return new Badge()
                .withBgColor(this.bg0 == null ? src.getBgColor().color0() : this.bg0)
                .withBgColor(this.bg1 == null ? src.getBgColor().color1() : this.bg1)
                .withCornerRadius(this.cornerRadius == null ? src.getCornerRadius() : this.cornerRadius)
                .withBorderColor(this.border0 == null ? src.getBorderColor().color0() : this.border0)
                .withBorderColor(this.border1 == null ? src.getBorderColor().color1() : this.border1)
                .withTextShadow(this.shadowColor == null ? src.getShadowColor() : this.shadowColor, this.shadowDir == null ? this.getShadowDir() : this.shadowDir)
                .withTexture(this.texture == null ? src.getTexture() : this.texture);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Badge badge)) return false;
        return Objects.equals(this.bg0, badge.bg0)
                && Objects.equals(this.bg1, badge.bg1)
                && Objects.equals(this.cornerRadius, badge.cornerRadius)
                && Objects.equals(this.border0, badge.border0)
                && Objects.equals(this.border1, badge.border1)
                && Objects.equals(this.shadowColor, badge.shadowColor)
                && Objects.equals(this.shadowDir, badge.shadowDir)
                && Objects.equals(this.texture, badge.texture);
    }

    public boolean isEmpty() {
        return this.bg0 == null && this.bg1 == null && this.cornerRadius == null && this.border0 == null && this.border1 == null && this.shadowColor == null && this.shadowDir == null && this.texture == null;
    }

    @Override
    public String toString() {
        StringCollector collector = new StringCollector();
        collector.collect("bg0", this.bg0)
                .collect("bg1", this.bg1)
                .collect("cornerRadius", this.cornerRadius)
                .collect("border0", this.border0)
                .collect("border1", this.border1)
                .collect("shadowColor", this.shadowColor)
                .collect("shadowDir", this.shadowDir)
                .collect("texture", this.texture);
        return collector.terminate().toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.bg0, this.bg1, this.cornerRadius, this.border0, this.border1, this.shadowColor, this.shadowDir, this.texture);
    }

    public static JsonObject serialize(@Nullable Badge badge) {
        JsonObject json = new JsonObject();
        if (badge != null) {
            if (badge.bg0 != null) json.addProperty("bg0", badge.bg0);
            if (badge.bg1 != null) json.addProperty("bg1", badge.bg1);
            if (badge.cornerRadius != null) json.addProperty("cornerRadius", badge.cornerRadius);
            if (badge.border0 != null) json.addProperty("border0", badge.border0);
            if (badge.border1 != null) json.addProperty("border1", badge.border1);
            if (badge.shadowColor != null) json.addProperty("shadowColor", badge.shadowColor);
            if (badge.shadowDir != null) json.addProperty("shadowDir", badge.shadowDir.getSerializedName());
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
            Integer cornerRadius = obj.has("cornerRadius") ? GsonHelper.getAsInt(obj, "cornerRadius") : null;
            Integer border0 = getColor(obj, "border0");
            Integer border1 = getColor(obj, "border1");
            Integer shadowColor = getColor(obj, "shadowColor");
            ShadowDir shadowDir = ShadowDir.getFromJson(obj, "shadowDir");
            ResourceLocation texture = getTexture(obj);
            return new Badge(bg0, bg1, cornerRadius, border0, border1, shadowColor, shadowDir, texture);
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

    public enum ShadowDir implements StringRepresentable {

        UP("up", new Vector3i(0, -1, 0)),
        DOWN("down", new Vector3i(0, 1, 0)),
        LEFT("left", new Vector3i(-1, 0, 0)),
        RIGHT("right", new Vector3i(1, 0, 0)),
        UPPER_LEFT("upper_left", new Vector3i(-1, -1, 0)),
        UPPER_RIGHT("upper_right", new Vector3i(1, -1, 0)),
        LOWER_LEFT("lower_left", new Vector3i(-1, 1, 0)),
        LOWER_RIGHT("lower_right", new Vector3i(1, 1, 0));

        private final String name;
        private final Vector3i translation;

        ShadowDir(String name, Vector3i translation) {
            this.name = name;
            this.translation = translation;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

        public @NotNull Vector3i getTranslation() {
            return this.translation;
        }

        private static @Nullable ShadowDir getFromJson(@NotNull JsonObject json, String key) {
            if (json.has(key)) {
                String name = GsonHelper.getAsString(json, key);
                try {
                    return ShadowDir.valueOf(name.toUpperCase());
                } catch (JsonSyntaxException e) {
                    throw new JsonSyntaxException("Invalid shadow direction: " + name);
                }
            }
            return null;
        }
    }
}
