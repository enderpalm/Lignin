package dev.enderpalm.lignin.text.container;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Badge {

    private final int data;

    public Badge(int data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.valueOf(this.data);
    }

    public static @Nullable Badge getBadge(@NotNull JsonObject json){
        if (json.has("badge")) {
            JsonObject jsonObject = json.getAsJsonObject("badge");
            return Badge.deserialize(jsonObject);
        }
        return null;
    }

    @Nullable
    public static Badge deserialize(JsonObject json){
        return new Badge(json.get("data").getAsInt());
    }
}
