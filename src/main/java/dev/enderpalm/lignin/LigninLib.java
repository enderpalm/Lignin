package dev.enderpalm.lignin;

import dev.enderpalm.lignin.util.Reference;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LigninLib implements ModInitializer {

    public static Logger LOG = LoggerFactory.getLogger("LigninLib");
    public static Reference REF = new Reference("lignin");

    @Override
    public void onInitialize() {
        LOG.info("LigninLib version {} is running (env: {}{})",
                REF.getModVersion(), Reference.isDevEnv() ? "dev" : "prod",
                Reference.isDevEnv() ? (", mock: " + !Reference.isMockDisabled()) : "");
    }
}
