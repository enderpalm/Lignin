package dev.enderpalm.lignin.test;

import dev.enderpalm.lignin.util.Reference;
import net.fabricmc.api.ModInitializer;

public class ReferenceTest implements ModInitializer {

    public static Reference REF = new Reference("lignin-testmod");

    @Override
    public void onInitialize() {
        REF.getAnonymousLogger().info("Lignin Testmod is running...");
        REF.getNamedLogger().info("Test Mod name: {}, Mod version: {}", REF.getModName(), REF.getModVersion());
        REF.getNamedLogger("ReferenceTestV2").info("ResourceLocation for 'dummy' is '{}'", REF.getPath("dummy"));
    }
}
