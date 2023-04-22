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

    public Integer getCornerRadius() {
        return this.cornerRadius != null ? this.cornerRadius.intValue() : 0;
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
            if (color.equals("auto")) return -1;
            if (color.startsWith("#"))
                return Integer.parseInt(color.substring(1), 16);
            else return Integer.parseInt(color,10);
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

    public static JsonObject serialize(@Nullable Badge badge) {
        JsonObject json = new JsonObject();
        if (badge != null){
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
            JsonObject obj = GsonHelper.getAsJsonObject(json,"badge");
            Integer bg0 = getColor(obj, "bg0");
            Integer bg1 = getColor(obj, "bg1");
            Byte cornerRadius = obj.has("cornerRadius") ? GsonHelper.getAsByte(obj, "cornerRadius") : null;
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

    public record ColorPair(@Nullable Integer color0, @Nullable Integer color1){

        public boolean isDisabled(){
            return color0 == null && color1 == null;
        }

        public boolean isAuto(){
            return (color0 != null && color0 == -1) || (color1 != null && color1 == -1);
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
