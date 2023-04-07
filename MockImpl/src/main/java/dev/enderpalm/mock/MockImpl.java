package dev.enderpalm.mock;

import dev.enderpalm.lignin.LigninLib;
import net.fabricmc.api.ModInitializer;

public class MockImpl implements ModInitializer {

    @Override
    public void onInitialize() {
        LigninLib.LOG.info("MockImpl is running");
    }
}
