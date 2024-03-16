package com.witchica.compactstorage.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.witchica.compactstorage.CompactStorage;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.CompletableFuture;

public class CompactStorageConfig {
    private File configFile;

    public class ConfigValues {
        public int maxIronStorageWidth = 24;
        public int maxIronStorageHeight = 12;
        public int maxBackpackWidth = 24;
        public int maxBackpackHeight = 12;
        public int  drumStackCount = 64;
        public int maxWoodenStorageWidth = 12;
        public int maxWoodenStorageHeight = 9;
    }

    public ConfigValues VALUES;
    private Gson gson;

    public CompactStorageConfig() {
        this.configFile = new File(Platform.getConfigFolder().toFile(), "compact_storage.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        if(!configFile.exists()) {
            createConfigFile();
        }

        ReloadListenerRegistry.register(PackType.SERVER_DATA, new ResourceManagerReloadListener() {
            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                reloadConfig();
            }
        });

        reloadConfig();
    }

    private void createConfigFile() {
        try {
            configFile.createNewFile();

            FileWriter writer = new FileWriter(this.configFile);
            gson.toJson(new ConfigValues(), writer);
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            CompactStorage.LOGGER.error("Error when creating configuration file, error type %s", ex.getClass().getName());
            ex.printStackTrace();
        }
    }

    public void reloadConfig() {
        CompactStorage.LOGGER.info("Reloading configuration file...");
        try {
            FileReader reader = new FileReader(this.configFile);
            VALUES = gson.fromJson(reader, ConfigValues.class);
            reader.close();
        } catch(Exception ex) {
            CompactStorage.LOGGER.error("Error when loading configuration file, error type %s", ex.getClass().getName());
            ex.printStackTrace();
        }
        CompactStorage.LOGGER.info("Configuration file reloaded!");
    }
}
