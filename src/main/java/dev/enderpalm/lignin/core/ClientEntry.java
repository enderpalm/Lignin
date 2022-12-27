package dev.enderpalm.lignin.core;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public final class ClientEntry implements ClientModInitializer {

    public static final String MOD_ID = "lignin";
    private static final ModMetadata METADATA = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata();;
    public static final String AUTHOR = METADATA.getAuthors().toString();
    public static final String LIGNIN_VERSION = METADATA.getVersion().getFriendlyString();

    public static final Logger LOG = LogUtils.getLogger();
    public static boolean DEV_ENV = FabricLoader.getInstance().isDevelopmentEnvironment();

    @Override
    public void onInitializeClient() {
        LOG.info("Lignin library: version {}", LIGNIN_VERSION);
    }

    public static @NotNull ResourceLocation path(String id){
        return new ResourceLocation(MOD_ID, id);
    }
}
