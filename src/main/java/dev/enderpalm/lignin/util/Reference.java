package dev.enderpalm.lignin.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public final class Reference {

    private final String modID;
    private final Collection<Person> authors;
    private final Collection<Person> contributors;
    private final String modVersion;

    /**
     * Creates a new {@link Reference} instance, using provided mod ID to get metadata
     * @param modID the mod ID
     */
    public Reference(String modID){
        this.modID = modID;
        ModMetadata metadata = FabricLoader.getInstance().getModContainer(this.modID).orElseThrow().getMetadata();
        this.authors = metadata.getAuthors();
        this.contributors = metadata.getContributors();
        this.modVersion = metadata.getVersion().getFriendlyString();
    }

    public String getModID(){
        return this.modID;
    }

    public String getModVersion(){
        return this.modVersion;
    }

    public Collection<Person> getAuthors(){
        return this.authors;
    }

    public Collection<Person> getContributors() {
        return contributors;
    }

    public static String getMinecraftVersion(){
        return Minecraft.getInstance().getLaunchedVersion();
    }

    /**
     * Checks if the mock implementation of LigninLib is <u>disabled</u>
     * @return <b>true</b> if <code>-Dlignin.disableMock</code> is set to <code>true</code>
     * or if the environment is not a development environment
     * @see #isDevEnv()
     */
    public static boolean isMockDisabled(){
        return Boolean.getBoolean("lignin.disableMock") || !isDevEnv();
    }

    /**
     * Checks if the environment is a dev environment using Fabric Loader
     * @return <b>true</b> if check passes
     */
    public static boolean isDevEnv(){
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    /**
     * Creates a new {@link ResourceLocation} with the mod ID as the namespace
     * @param id the path of the resource
     * @return the new {@link ResourceLocation}
     */
    public @NotNull ResourceLocation path(String id){
        return new ResourceLocation(this.modID, id);
    }

    /**
     * Creates a new {@link ResourceLocation} with the mod ID as the namespace
     * @param id the path of the resource
     * @return String representation of the {@link ResourceLocation}
     */
    public @NotNull String pathLiteral(String id){
        return String.format("%s:%s",this.modID,id);
    }

}
