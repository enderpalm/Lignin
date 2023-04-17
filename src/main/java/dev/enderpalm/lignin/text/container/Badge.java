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
    @Nullable private final Byte cornerRadius;
    @Nullable private final Integer border0;
    @Nullable private final Integer border1;
    @Nullable private final Integer shadowColor;
    @Nullable private final ShadowDir shadowDir;
    @Nullable private final ResourceLocation texture;

    Badge(@Nullable Integer bg0, @Nullable Integer bg1, @Nullable Byte cornerRadius, @Nullable Integer border0, @Nullable Integer border1, @Nullable Integer shadowColor, @Nullable ShadowDir shadowDir, @Nullable ResourceLocation texture) {
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

    public Badge withBgColor(@Nullable Integer bgColor) {
        if (this.bg0 != null && this.bg1 != null) return this;
        if (this.bg0 == null)
            return new Badge(bgColor, this.bg1, this.cornerRadius, this.border0, this.border1, this.shadowColor, this.shadowDir, this.texture);
        return new Badge(this.bg0, bgColor, this.cornerRadius, this.border0, this.border1, this.shadowColor, this.shadowDir, this.texture);
    }

    public Badge withBgColor(@Nullable String bgColor) {
        return this.withBgColor(parseColor(bgColor));
    }

    public Badge withCornerRadius(int cornerRadius) {
        return this.innerWithCornerRadius((byte) cornerRadius);
    }

    private Badge innerWithCornerRadius(@Nullable Byte cornerRadius) {
        return new Badge(this.bg0, this.bg1, cornerRadius, this.border0, this.border1, this.shadowColor, this.shadowDir, this.texture);
    }

    public Badge withBorderColor(@Nullable Integer borderColor) {
        if (this.border0 != null && this.border1 != null) return this;
        if (this.border0 == null)
            return new Badge(this.bg0, this.bg1, this.cornerRadius, borderColor, this.border1, this.shadowColor, this.shadowDir, this.texture);
        return new Badge(this.bg0, this.bg1, this.cornerRadius, this.border0, borderColor, this.shadowColor, this.shadowDir, this.texture);
    }

    public Badge withBorderColor(@Nullable String borderColor) {
        return this.withBorderColor(parseColor(borderColor));
    }

    public Badge withTextShadow(@Nullable Integer shadowColor, @Nullable ShadowDir shadowDir) {
        return new Badge(this.bg0, this.bg1, this.cornerRadius, this.border0, this.border1, shadowColor, shadowDir, this.texture);
    }

    public Badge withTexture(@Nullable ResourceLocation texture) {
        return new Badge(this.bg0, this.bg1, this.cornerRadius, this.border0, this.border1, this.shadowColor, this.shadowDir, texture);
    }

    private static @Nullable Integer parseColor(@Nullable String color) {
        if (color != null) {
            if (color.equals("auto")) return -1;
            if (color.startsWith("#")) {
                return Integer.parseInt(color.substring(1), 16);
            }
        }
        return null;
    }

    public Badge mergeFrom(@Nullable Badge badge) {
        if (badge == null || this.equals(badge)) return this;
        return new Badge(
                badge.bg0 != null ? badge.bg0 : this.bg0,
                badge.bg1 != null ? badge.bg1 : this.bg1,
                badge.cornerRadius != null ? badge.cornerRadius : this.cornerRadius,
                badge.border0 != null ? badge.border0 : this.border0,
                badge.border1 != null ? badge.border1 : this.border1,
                badge.shadowColor != null ? badge.shadowColor : this.shadowColor,
                badge.shadowDir != null ? badge.shadowDir : this.shadowDir,
                badge.texture != null ? badge.texture : this.texture
        );
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

    @Nullable
    public static Badge deserialize(@NotNull JsonObject json) {
        if (!json.has("badge")) return null;
        Badge badge = new Badge();
        JsonObject obj = json.getAsJsonObject("badge");
        badge.withBgColor(getColor(obj, "bg0")).withBgColor(getColor(obj, "bg1"));
        badge.innerWithCornerRadius(obj.has("cornerRadius") ? obj.get("cornerRadius").getAsByte() : null);
        badge.withBorderColor(getColor(obj, "border0")).withBorderColor(getColor(obj, "border1"));
        badge.withTextShadow(getColor(obj, "shadowColor"), ShadowDir.getFromJson(obj, "shadowDir"));
        badge.withTexture(getTexture(obj));
        return badge;
    }

    @Nullable
    private static Integer getColor(JsonObject json, String key) {
        return json.has(key) ? parseColor(GsonHelper.getAsString(json, key)) : null;
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
