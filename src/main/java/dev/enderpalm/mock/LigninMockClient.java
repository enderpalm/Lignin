package dev.enderpalm.mock;

import dev.enderpalm.lignin.util.Reference;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class LigninMockClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        if (Reference.isMockDisabled()) return;
    }
}
