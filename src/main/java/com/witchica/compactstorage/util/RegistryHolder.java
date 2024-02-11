package com.witchica.compactstorage.util;

import com.witchica.compactstorage.CompactStorage;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegistryHolder<V> {

    public static class Blocks extends RegistryHolder<Block> {
        public Blocks(Identifier key, Block value) {
            super(key, value);
        }

        public Blocks(String name, Block value) {
            super(name, value);
        }

        public void registerBlockItem() {
            Registry.register(Registries.ITEM, getKey(), new BlockItem(get(), new FabricItemSettings()));
        }

        public void registerBlockAndItem() {
            register(Registries.BLOCK);
            registerBlockItem();
        }
    }

    public static class Items extends RegistryHolder<Item> {
        public Items(Identifier key, Item value) {
            super(key, value);
        }

        public Items(String name, Item value) {
            super(name, value);
        }
    }

    private final Identifier key;
    private final V value;
    public RegistryHolder(Identifier key, V value) {
        this.key = key;
        this.value = value;
    }

    public RegistryHolder(String name, V value) {
        this(new Identifier(CompactStorage.MOD_ID, name), value);
    }

    public void register(Registry registry) {
        Registry.register(registry, key, value);
    }

    public Identifier getKey() {
        return key;
    }

    public V get() {
        return value;
    }
}
