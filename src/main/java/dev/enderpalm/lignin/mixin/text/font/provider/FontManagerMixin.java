package dev.enderpalm.lignin.mixin.text.font.provider;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.font.GlyphProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(targets = "net/minecraft/client/gui/font/FontManager$1")
public abstract class FontManagerMixin {

    @Inject(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/Map;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/GsonHelper;getAsString(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/String;"),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void appendSrcFontToJson(ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfoReturnable<Map<ResourceLocation, List<GlyphProvider>>> cir,
                                     Gson gson, Map<?, ?> map, Iterator<?> var5, Map.Entry<?, ?> entry, ResourceLocation resourceLocation, ResourceLocation resourceLocation2,
                                     List<?> list, Iterator<?> var10, Resource resource, Reader reader, JsonArray jsonArray, int i, JsonObject jsonObject){
        jsonObject.addProperty("src", resourceLocation2.toString());
    }
}
