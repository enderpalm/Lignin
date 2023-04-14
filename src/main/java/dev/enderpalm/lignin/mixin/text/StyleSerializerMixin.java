package dev.enderpalm.lignin.mixin.text;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.enderpalm.lignin.text.StyleInjector;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.reflect.Type;

@Mixin(Style.Serializer.class)
public abstract class StyleSerializerMixin implements StyleInjector {

    @Inject(method = "serialize(Lnet/minecraft/network/chat/Style;Ljava/lang/reflect/Type;Lcom/google/gson/JsonSerializationContext;)Lcom/google/gson/JsonElement;",
        at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void injectSerialize(Style style, Type type, JsonSerializationContext jsonSerializationContext, CallbackInfoReturnable<JsonElement> cir, JsonObject jsonObject){
        System.out.println("Injecting badge");
        if(style.getBadge() != null){
            System.out.println(style.getBadge());
            jsonObject.add("badge", jsonSerializationContext.serialize(style.getBadge()));
        }
    }
}
