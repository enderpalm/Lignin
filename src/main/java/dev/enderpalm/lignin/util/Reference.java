package dev.enderpalm.lignin.util;

import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Reference {

    private final String modID;
    private final ModMetadata modMetadata;
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public Reference(@NotNull String modID) {
        this.modID = modID;
        var container = FabricLoader.getInstance().getModContainer(modID);
        if (container.isEmpty())
            throw new JsonParseException(String.format("Couldn't create Reference instance due to Mod with ID: \"%s\" not found :(", this.modID));
        this.modMetadata = container.get().getMetadata();
    }

    public String getModName() {
        return this.modMetadata.getName();
    }

    public String getModVersion(){
        return this.modMetadata.getVersion().getFriendlyString();
    }

    public @NotNull ResourceLocation getPath(@NotNull String path) {
        return new ResourceLocation(this.modID, path);
    }

    public @NotNull Logger getNamedLogger(@NotNull String entry){
        String component = this.getModName() + " | " + entry;
        return LoggerFactory.getLogger(component);
    }

    public @NotNull Logger getNamedLogger(){
        String component = this.getModName() + " | " + STACK_WALKER.getCallerClass().getSimpleName();
        return LoggerFactory.getLogger(component);
    }

    public @NotNull Logger getAnonymousLogger(){
        return LoggerFactory.getLogger(this.getModName());
    }

    public static boolean isDevEnv(){
        return FabricLoader.getInstance().isDevelopmentEnvironment() || Boolean.getBoolean("lignin.debug");
    }

}
