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

    public static boolean isDevEnv(){
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public @NotNull ResourceLocation path(String id){
        return new ResourceLocation(this.modID, id);
    }

    public @NotNull String pathLiteral(String id){
        return String.format("%s:%s",this.modID,id);
    }


}
