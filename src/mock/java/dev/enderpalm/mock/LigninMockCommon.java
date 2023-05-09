package dev.enderpalm.mock;

import dev.enderpalm.lignin.util.Reference;
import net.fabricmc.api.ModInitializer;

public class LigninMockCommon implements ModInitializer {

    public static final Reference REF = new Reference("lignin-mock");

    @Override
    public void onInitialize() {
        REF.getAnonymousLogger().info("Lignin Mock is running...");
    }
}
