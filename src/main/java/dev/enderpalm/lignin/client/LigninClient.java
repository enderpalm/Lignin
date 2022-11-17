package dev.enderpalm.lignin.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class LigninClient implements ClientModInitializer {

    public static final String MOD_ID = "lignin";
    private static final ModMetadata METADATA = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata();;
    public static final String AUTHOR = METADATA.getAuthors().toString();
    public static final String LIGNIN_VERSION = METADATA.getVersion().getFriendlyString();
    public static final Logger LOG = LoggerFactory.getLogger("Lignin");

    @Override
    public void onInitializeClient() {
        LOG.info("Lignin library: version {}", LIGNIN_VERSION);
    }
}
