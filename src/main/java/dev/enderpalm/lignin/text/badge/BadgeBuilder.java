package dev.enderpalm.lignin.text.badge;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import dev.enderpalm.lignin.core.ClientEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class BadgeBuilder {

    private final String signature = ClientEntry.INSTANCE_SIGNATURE;
    private final boolean translatable;
    private final String literal;

    public BadgeBuilder(String literal, boolean translatable) {
        this.literal = literal;
        this.translatable = translatable;
    }

    private Component serialize(@NotNull BadgeType type, @NotNull List<Pair<String, ?>> entry) {
        JsonObject json = new JsonObject();
        json.addProperty("signature", this.signature);
        json.addProperty("literal", this.literal);
        json.addProperty("translatable", this.translatable);
        json.addProperty("type", type.getId());
        for (Pair<String, ?> pair : entry) {
            var value = pair.getSecond();
            if (value instanceof Integer) json.addProperty(pair.getFirst(), Integer.parseInt(value.toString()));
            else if (value instanceof Boolean) json.addProperty(pair.getFirst(), Boolean.valueOf(value.toString()));
            else json.addProperty(pair.getFirst(), value.toString());
        }
        return Component.literal(json.toString());
    }

    public Component fillSolid(int color, boolean shadow) {
        List<Pair<String, ?>> properties = new ArrayList<>();
        properties.add(new Pair<>("color", color));
        properties.add(new Pair<>("shadow", shadow));
        return this.serialize(BadgeType.FILL_SOLID, properties);
    }


    /*
    public String decompose(FormattedCharSequence sequence){
        StringBuilder builder = new StringBuilder();
        sequence.accept((i, style, j) -> {
            builder.append((char) j);
            return true;
        });
        return builder.toString();
    }

     */

    public enum BadgeType {

        FILL_SOLID(0);

        private final int id;

        BadgeType(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }
    }

}
