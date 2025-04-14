package com.witchica.compactstorage.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.witchica.compactstorage.common.CompactStorage;
import dev.architectury.platform.Platform;
import org.apache.logging.log4j.core.config.yaml.YamlConfiguration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class CompactStorageConfig {
    private final File configFile;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public boolean useChests = true;
    public boolean useWoodenChests = true;
    public boolean useBarrels = true;
    public boolean useWoodenBarrels = true;
    public boolean useDrums = true;
    public boolean useWoodenDrums = true;
    public boolean useBackpacks = true;
    public boolean useWoodenBackpacks = true;

    public int maximumInventoryWidth = 21;
    public int maximumInventoryHeight = 12;
    
    public CompactStorageConfig() {
        this.configFile = new File(Platform.getConfigFolder().toFile(), "compact_storage.json");

        if(!this.configFile.getParentFile().exists()) {
            this.configFile.getParentFile().mkdirs();
        }
    }
    
    public <T> T getOrDefault(JsonObject object, String key, T defaultValue) {
        if (object.has(key)) {
            Object value = gson.fromJson(object.get(key), defaultValue.getClass());

            if(value != null) {
                return (T) value;
            }
        }

        return defaultValue;
    }

    public void readConfig() {
        try {
            if(configFile.exists()) {
                FileReader fileReader = new FileReader(this.configFile);
                JsonObject object = gson.fromJson(fileReader, JsonObject.class);

                useChests = getOrDefault(object, "bUseChests", useChests);
                useWoodenChests = getOrDefault(object, "bUseWoodenChests", useWoodenChests);
                useBarrels = getOrDefault(object, "bUseBarrels", useBarrels);
                useWoodenBarrels = getOrDefault(object, "bUseWoodenBarrels", useWoodenBarrels);
                useDrums = getOrDefault(object, "bUseDrums", useDrums);
                useWoodenDrums = getOrDefault(object, "bUseWoodenDrums", useWoodenDrums);
                useBackpacks = getOrDefault(object, "bUseBackpacks", useBackpacks);
                useWoodenBackpacks = getOrDefault(object, "bUseWoodenBackpacks", useWoodenBackpacks);

                maximumInventoryWidth = getOrDefault(object, "maximumInventoryWidth", maximumInventoryWidth);
                maximumInventoryWidth = Math.min(maximumInventoryWidth, 21);
                maximumInventoryWidth = Math.max(maximumInventoryWidth, 9);

                maximumInventoryHeight = getOrDefault(object, "maximumInventoryHeight", maximumInventoryHeight);
                maximumInventoryHeight = Math.min(maximumInventoryHeight, 12);
                maximumInventoryHeight = Math.max(maximumInventoryHeight, 6);

                fileReader.close();
            }
        } catch (Exception ex) {
            CompactStorage.LOGGER.error("Error reading configuration file");
            ex.printStackTrace();
        }

        saveConfig();
    }

    public void saveConfig() {
        try {
            if(!this.configFile.exists()) {
                this.configFile.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(this.configFile);

            JsonObject configObject = new JsonObject();
            configObject.addProperty("bUseChests", useChests);
            configObject.addProperty("useWoodenChests", useWoodenChests);
            configObject.addProperty("bUseBarrels", useBarrels);
            configObject.addProperty("bUseWoodenBarrels", useWoodenBarrels);
            configObject.addProperty("bUseDrums", useDrums);
            configObject.addProperty("bUseWoodenDrums", useWoodenDrums);
            configObject.addProperty("bUseBackpacks", useBackpacks);
            configObject.addProperty("bUseWoodenBackpacks", useWoodenBackpacks);

            configObject.addProperty("maximumInventoryWidth", maximumInventoryWidth);
            configObject.addProperty("maximumInventoryHeight", maximumInventoryHeight);

            gson.toJson(configObject, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception ex) {
            CompactStorage.LOGGER.error("Error writing configuration file");
            ex.printStackTrace();
        }
    }
}
