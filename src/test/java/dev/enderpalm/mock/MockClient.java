package dev.enderpalm.mock;

import dev.enderpalm.lignin.util.Reference;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MockClient implements ClientModInitializer {

    Reference reference = new Reference("lignin-mock");

    @Override
    public void onInitializeClient() {
        System.out.println("MockClient.onInitializeClient()");
    }
}
