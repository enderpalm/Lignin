package dev.enderpalm.mock;

import dev.enderpalm.lignin.util.Reference;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LigninMockCommon implements ModInitializer {

    public static final Logger LOG = LoggerFactory.getLogger("LigninLibMock");

    @Override
    public void onInitialize() {
        if (Reference.isMockDisabled()) return;
        LOG.warn("LigninLib mock implementation is running...");

    }
}
