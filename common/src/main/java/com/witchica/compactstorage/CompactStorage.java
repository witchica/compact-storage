package com.witchica.compactstorage;

import com.mojang.logging.LogUtils;
import com.witchica.compactstorage.common.block.CompactBarrelBlock;
import com.witchica.compactstorage.common.block.CompactChestBlock;
import com.witchica.compactstorage.common.block.DrumBlock;
import com.witchica.compactstorage.common.block.entity.CompactBarrelBlockEntity;
import com.witchica.compactstorage.common.block.entity.CompactChestBlockEntity;
import com.witchica.compactstorage.common.block.entity.DrumBlockEntity;
import com.witchica.compactstorage.common.item.BackpackItem;
import com.witchica.compactstorage.common.item.StorageUpgradeItem;
import com.witchica.compactstorage.common.screen.CompactChestScreenHandler;
import com.witchica.compactstorage.common.util.CompactStorageUtil;
import com.witchica.compactstorage.common.util.StorageUpgradeType;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class CompactStorage {
    public static final String MOD_ID = "compact_storage";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, Registries.BLOCK);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(MOD_ID, Registries.MENU);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static ResourceLocation COMPACT_CHEST_GENERIC_IDENTIFIER = new ResourceLocation(MOD_ID, "compact_chest");
    public static ResourceLocation COMPACT_BARREL_GENERIC_IDENTIFIER = new ResourceLocation(MOD_ID, "compact_barrel");

    public static final RegistrySupplier<CompactChestBlock>[] COMPACT_CHEST_BLOCKS = new RegistrySupplier[CompactStorageUtil.StorageVisualTypes.values().length];
    public static final RegistrySupplier<CompactBarrelBlock>[] COMPACT_BARREL_BLOCKS = new RegistrySupplier[CompactStorageUtil.StorageVisualTypes.values().length];
    public static final RegistrySupplier<DrumBlock>[] DRUM_BLOCKS = new RegistrySupplier[CompactStorageUtil.StorageVisualTypes.values().length];
    public static final RegistrySupplier<BackpackItem>[] BACKPACK_ITEMS = new RegistrySupplier[16];

    private static Block[] getAllCompactChests() {
        List<Block> blocks = new ArrayList<Block>();
        Arrays.stream(COMPACT_CHEST_BLOCKS).forEach(block -> blocks.add(block.get()));

        return blocks.toArray(Block[]::new);
    }
    private static Block[] getAllCompactBarrels() {
        List<Block> blocks = new ArrayList<Block>();
        Arrays.stream(COMPACT_BARREL_BLOCKS).forEach(block -> blocks.add(block.get()));

        return blocks.toArray(Block[]::new);
    }

    public static RegistrySupplier<BlockEntityType<CompactBarrelBlockEntity>> COMPACT_BARREL_ENTITY_TYPE =
            BLOCK_ENTITY_TYPES.register("compact_barrel", () -> BlockEntityType.Builder.of(CompactStoragePlatform.compactBarrelBlockEntityProvider(), getAllCompactBarrels()).build(null));

    public static RegistrySupplier<BlockEntityType<CompactChestBlockEntity>> COMPACT_CHEST_ENTITY_TYPE =
            BLOCK_ENTITY_TYPES.register("compact_chest", () -> BlockEntityType.Builder.of(CompactStoragePlatform.compactChestBlockEntityProvider(), getAllCompactChests()).build(null));

    public static RegistrySupplier<BlockEntityType<DrumBlockEntity>> DRUM_ENTITY_TYPE =
            BLOCK_ENTITY_TYPES.register("drum", () -> BlockEntityType.Builder.of(CompactStoragePlatform.drumBlockEntityProvider(), Arrays.stream(DRUM_BLOCKS).map(RegistrySupplier::get).toArray(Block[]::new)).build(null));

    public static final HashMap<DyeColor, RegistrySupplier<CompactChestBlock>> DYE_COLOR_TO_COMPACT_CHEST_MAP = new HashMap<DyeColor, RegistrySupplier<CompactChestBlock>>();
    public static final HashMap<DyeColor, RegistrySupplier<CompactBarrelBlock>> DYE_COLOR_TO_COMPACT_BARREL_MAP = new HashMap<DyeColor, RegistrySupplier<CompactBarrelBlock>>();
    public static final HashMap<DyeColor, RegistrySupplier<BackpackItem>> DYE_COLOR_TO_BACKPACK_MAP = new HashMap<DyeColor, RegistrySupplier<BackpackItem>>();
    public static final HashMap<DyeColor, RegistrySupplier<DrumBlock>> DYE_COLOR_TO_DRUM_MAP = new HashMap<DyeColor, RegistrySupplier<DrumBlock>>();

    public static final RegistrySupplier<StorageUpgradeItem> UPGRADE_ROW_ITEM = ITEMS.register("upgrade_row", () -> new StorageUpgradeItem(StorageUpgradeType.ROW, new Item.Properties()));
    public static final RegistrySupplier<StorageUpgradeItem> UPGRADE_COLUMN_ITEM = ITEMS.register("upgrade_column", () -> new StorageUpgradeItem(StorageUpgradeType.COLUMM, new Item.Properties()));
    public static final RegistrySupplier<StorageUpgradeItem> UPGRADE_RETAINER_ITEM = ITEMS.register("upgrade_retainer", () -> new StorageUpgradeItem(StorageUpgradeType.RETAINING, new Item.Properties()));

    public static RegistrySupplier<MenuType<CompactChestScreenHandler>> COMPACT_CHEST_SCREEN_HANDLER = MENU_TYPES.register("compact_chest", () -> MenuRegistry.ofExtended(CompactChestScreenHandler::new));

    //public static RegistrySupplier<Item> WRENCH_ITEM = ITEMS.register("wrench", () -> new WrenchItem(new Item.Properties().stacksTo(1)));
    public static final RegistrySupplier<CreativeModeTab> COMPACT_STORAGE_TAB = CREATIVE_MODE_TABS.register("compact_storage_tab", () -> CreativeTabRegistry.create(builder -> {
        builder.title(Component.translatable("itemGroup.compact_storage.general"))
                .icon(() -> new ItemStack(COMPACT_CHEST_BLOCKS[25].get(), 1))
                .displayItems((params, populator) -> {
                    populator.accept(UPGRADE_COLUMN_ITEM.get());
                    populator.accept(UPGRADE_ROW_ITEM.get());
                    populator.accept(UPGRADE_RETAINER_ITEM.get());

                    Arrays.stream(COMPACT_CHEST_BLOCKS).map(Supplier::get).filter(block -> !block.getVisualType().isWooden()).forEach(populator::accept);
                    Arrays.stream(COMPACT_BARREL_BLOCKS).map(Supplier::get).filter(block -> !block.getVisualType().isWooden()).forEach(populator::accept);
                    Arrays.stream(DRUM_BLOCKS).map(Supplier::get).filter(block -> !block.getType().isWooden()).forEach(populator::accept);
                    Arrays.stream(BACKPACK_ITEMS).map(Supplier::get).forEach(populator::accept);
                });
    }));

    public static final RegistrySupplier<CreativeModeTab> COMPACT_STORAGE_WOOD_TAB = CREATIVE_MODE_TABS.register("compact_storage_tab_wood", () -> CreativeTabRegistry.create(builder -> {
        builder.title(Component.translatable("itemGroup.compact_storage.wood"))
                .icon(() -> new ItemStack(COMPACT_CHEST_BLOCKS[1].get(), 1))
                .displayItems((params, populator) -> {

                    Arrays.stream(COMPACT_CHEST_BLOCKS).map(Supplier::get).filter(block -> block.getVisualType().isWooden()).forEach(populator::accept);
                    Arrays.stream(COMPACT_BARREL_BLOCKS).map(Supplier::get).filter(block -> block.getVisualType().isWooden()).forEach(populator::accept);
                    Arrays.stream(DRUM_BLOCKS).map(Supplier::get).filter(block -> block.getType().isWooden()).forEach(populator::accept);
                });
    }));

    static {
        int backpackIndex = 0;
        int drumIndex = 0;
        for(int i = 0; i < CompactStorageUtil.StorageVisualTypes.values().length; i++) {
            CompactStorageUtil.StorageVisualTypes type = CompactStorageUtil.StorageVisualTypes.values()[i];
            String name = type.getType();
            int index = i;

            String chestName = "compact_chest_" + name;
            String barrelName = "compact_barrel_" + name;
            String backpackName = "backpack_" + name;
            String drumName = name + "_drum";

            if(type.isWooden()) {
                chestName = name+"_compact_chest";
                barrelName = name+"_compact_barrel";
            }

            COMPACT_CHEST_BLOCKS[i] = BLOCKS.register(chestName, () ->
                    new CompactChestBlock(type)
            );
            ITEMS.register(chestName, () ->
                    new BlockItem(COMPACT_CHEST_BLOCKS[index].get(), new Item.Properties())
            );

            if(!type.isWooden()) {
                DYE_COLOR_TO_COMPACT_CHEST_MAP.put(type.getAssociatedDyeColor(), COMPACT_CHEST_BLOCKS[i]);

                BACKPACK_ITEMS[backpackIndex] = ITEMS.register(backpackName, () ->
                        new BackpackItem(type, new Item.Properties().stacksTo(1)));
                DYE_COLOR_TO_BACKPACK_MAP.put(type.getAssociatedDyeColor(), BACKPACK_ITEMS[backpackIndex]);
                backpackIndex++;
            }


            COMPACT_BARREL_BLOCKS[i] = BLOCKS.register(barrelName, () ->
                    new CompactBarrelBlock(type));

            ITEMS.register(barrelName, () ->
                    new BlockItem(COMPACT_BARREL_BLOCKS[index].get(), new Item.Properties())
            );

            if(!type.isWooden()) {
                DYE_COLOR_TO_COMPACT_BARREL_MAP.put(type.getAssociatedDyeColor(), COMPACT_BARREL_BLOCKS[i]);
            }

            DRUM_BLOCKS[index] = BLOCKS.register(drumName, () ->
                    new DrumBlock(type));

            ITEMS.register(drumName, () ->
                    new BlockItem(DRUM_BLOCKS[index].get(), new Item.Properties())
            );

            if(!type.isWooden()) {
                DYE_COLOR_TO_DRUM_MAP.put(type.getAssociatedDyeColor(), DRUM_BLOCKS[index]);
            }
        }
    }

    public static void onInitialize() {
        if(Platform.getEnv() == EnvType.CLIENT) {
            ClientLifecycleEvent.CLIENT_SETUP.register(CompactStorageCommonClient::clientSetupEvent);
        }

        BLOCKS.register();
        ITEMS.register();
        BLOCK_ENTITY_TYPES.register();
        MENU_TYPES.register();
        CREATIVE_MODE_TABS.register();
    }

    public static Block getCompactBarrelFromDyeColor(DyeColor dye) {
        return DYE_COLOR_TO_COMPACT_BARREL_MAP.get(dye).get();
    }
    public static Block getCompactChestFromDyeColor(DyeColor dye) {
        return DYE_COLOR_TO_COMPACT_CHEST_MAP.get(dye).get();
    }

    public static Item getBackpackFromDyeColor(DyeColor dye) {
        return DYE_COLOR_TO_BACKPACK_MAP.get(dye).get();
    }

    public static Block getDrumFromDyeColor(DyeColor dye) {
        return DYE_COLOR_TO_DRUM_MAP.get(dye).get();
    }
}
