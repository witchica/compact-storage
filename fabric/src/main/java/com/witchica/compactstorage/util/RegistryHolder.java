package com.witchica.compactstorage.util;

import com.witchica.compactstorage.CompactStorage;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class RegistryHolder<V> {

    public static class Blocks extends RegistryHolder<Block> {
        public Blocks(ResourceLocation key, Block value) {
            super(key, value);
        }

        public Blocks(String name, Block value) {
            super(name, value);
        }

        public void registerBlockItem() {
            Registry.register(BuiltInRegistries.ITEM, getKey(), new BlockItem(get(), new FabricItemSettings()));
        }

        public void registerBlockAndItem() {
            register(BuiltInRegistries.BLOCK);
            registerBlockItem();
        }
    }

    public static class Items extends RegistryHolder<Item> {
        public Items(ResourceLocation key, Item value) {
            super(key, value);
        }

        public Items(String name, Item value) {
            super(name, value);
        }
    }

    private final ResourceLocation key;
    private final V value;
    public RegistryHolder(ResourceLocation key, V value) {
        this.key = key;
        this.value = value;
    }

    public RegistryHolder(String name, V value) {
        this(new ResourceLocation(CompactStorage.MOD_ID, name), value);
    }

    public void register(Registry registry) {
        Registry.register(registry, key, value);
    }

    public ResourceLocation getKey() {
        return key;
    }

    public V get() {
        return value;
    }
}
