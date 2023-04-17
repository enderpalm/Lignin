package dev.enderpalm.lignin.mixin.text;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.enderpalm.lignin.text.container.Badge;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.reflect.Type;

@Mixin(Style.Serializer.class)
public abstract class StyleSerializerMixin {

    @Inject(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/network/chat/Style;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Style;<init>(Lnet/minecraft/network/chat/TextColor;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Ljava/lang/Boolean;Lnet/minecraft/network/chat/ClickEvent;Lnet/minecraft/network/chat/HoverEvent;Ljava/lang/String;Lnet/minecraft/resources/ResourceLocation;)V"),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext, @NotNull CallbackInfoReturnable<Style> cir, JsonObject jsonObject , Boolean boolean_, Boolean boolean_2, Boolean boolean_3, Boolean boolean_4, Boolean boolean_5, TextColor textColor, String string, ClickEvent clickEvent, HoverEvent hoverEvent, ResourceLocation resourceLocation){
        Badge badge = Badge.deserialize(jsonObject);
        cir.setReturnValue(new Style(textColor, boolean_, boolean_2, boolean_3, boolean_4, boolean_5, clickEvent, hoverEvent, string, resourceLocation).withBadge(badge));
    }

    @Inject(method = "serialize(Lnet/minecraft/network/chat/Style;Ljava/lang/reflect/Type;Lcom/google/gson/JsonSerializationContext;)Lcom/google/gson/JsonElement;",
            at = @At(value = "TAIL", shift = At.Shift.BEFORE), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void serialize(Style style, Type type, JsonSerializationContext jsonSerializationContext, CallbackInfoReturnable<JsonElement> cir, JsonObject jsonObject){
        cir.setReturnValue(jsonObject);
    }
}
