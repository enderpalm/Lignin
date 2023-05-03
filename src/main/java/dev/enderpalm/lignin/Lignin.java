package dev.enderpalm.lignin;

import dev.enderpalm.lignin.util.Reference;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class Lignin implements ModInitializer {

    public static final Reference REF = new Reference("lignin");
    private static final Logger LOG = REF.getAnonymousLogger();

    @Override
    public void onInitialize() {
        LOG.info("Lignin library: version {} (env: {})", REF.getModVersion(), Reference.isDevEnv() ? "dev" : "prod");
    }
}
