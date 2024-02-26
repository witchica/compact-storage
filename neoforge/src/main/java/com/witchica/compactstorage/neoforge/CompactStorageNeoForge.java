package com.witchica.compactstorage.neoforge;

import com.mojang.logging.LogUtils;
import com.witchica.compactstorage.common.block.CompactBarrelBlock;
import com.witchica.compactstorage.common.block.CompactChestBlock;
import com.witchica.compactstorage.common.block.DrumBlock;
import com.witchica.compactstorage.common.block.entity.CompactBarrelBlockEntity;
import com.witchica.compactstorage.common.block.entity.CompactChestBlockEntity;
import com.witchica.compactstorage.common.block.entity.DrumBlockEntity;
import com.witchica.compactstorage.common.inventory.BackpackInventoryHandlerFactory;
import com.witchica.compactstorage.common.item.BackpackItem;
import com.witchica.compactstorage.common.item.StorageUpgradeItem;
import com.witchica.compactstorage.common.screen.CompactChestScreenHandler;
import com.witchica.compactstorage.common.util.CompactStorageUtil;
import com.witchica.compactstorage.neoforge.block.NeoForgeCompactBarrelBlock;
import com.witchica.compactstorage.neoforge.block.NeoForgeCompactChestBlock;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.witchica.compactstorage.neoforge.CompactStorageNeoForge.MOD_ID;

@Mod(MOD_ID)
public class CompactStorageNeoForge {
    public static final String MOD_ID = "compact_storage";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.Blocks.createBlocks(MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.Items.createItems(MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MOD_ID);
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, MOD_ID);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, MOD_ID);

    public static ResourceLocation COMPACT_CHEST_GENERIC_IDENTIFIER = new ResourceLocation(MOD_ID, "compact_chest");
    public static ResourceLocation COMPACT_BARREL_GENERIC_IDENTIFIER = new ResourceLocation(MOD_ID, "compact_barrel");

    public static final DeferredBlock<NeoForgeCompactChestBlock>[] COMPACT_CHEST_BLOCKS = new DeferredBlock[16];
    public static final DeferredBlock<NeoForgeCompactBarrelBlock>[] COMPACT_BARREL_BLOCKS = new DeferredBlock[16];
    public static final DeferredBlock<DrumBlock>[] DRUM_BLOCKS = new DeferredBlock[CompactStorageUtil.DRUM_TYPES.length];
    public static final DeferredItem<BackpackItem>[] BACKPACK_ITEMS = new DeferredItem[16];

    public static final String COMPACT_CHEST_TRANSLATION_KEY = Util.makeDescriptionId("container", COMPACT_CHEST_GENERIC_IDENTIFIER);

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<CompactBarrelBlockEntity>> COMPACT_BARREL_ENTITY_TYPE =
            BLOCK_ENTITY_TYPES.register("compact_barrel", () -> BlockEntityType.Builder.of(CompactBarrelBlockEntity::new, Arrays.stream(COMPACT_BARREL_BLOCKS).map(DeferredBlock::get).toArray(Block[]::new)).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<CompactChestBlockEntity>> COMPACT_CHEST_ENTITY_TYPE =
            BLOCK_ENTITY_TYPES.register("compact_chest", () -> BlockEntityType.Builder.of(CompactChestBlockEntity::new, Arrays.stream(COMPACT_CHEST_BLOCKS).map(DeferredBlock::get).toArray(Block[]::new)).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<DrumBlockEntity>> DRUM_ENTITY_TYPE =
            BLOCK_ENTITY_TYPES.register("drum", () -> BlockEntityType.Builder.of(DrumBlockEntity::new, Arrays.stream(DRUM_BLOCKS).map(DeferredBlock::get).toArray(Block[]::new)).build(null));

    public static final HashMap<DyeColor, DeferredBlock<NeoForgeCompactChestBlock>> DYE_COLOR_TO_COMPACT_CHEST_MAP = new HashMap<DyeColor, DeferredBlock<NeoForgeCompactChestBlock>>();
    public static final HashMap<DyeColor, DeferredBlock<NeoForgeCompactBarrelBlock>> DYE_COLOR_TO_COMPACT_BARREL_MAP = new HashMap<DyeColor, DeferredBlock<NeoForgeCompactBarrelBlock>>();
    public static final HashMap<DyeColor, DeferredItem<BackpackItem>> DYE_COLOR_TO_BACKPACK_MAP = new HashMap<DyeColor, DeferredItem<BackpackItem>>();

    public static final DeferredItem<Item> UPGRADE_ROW_ITEM = ITEMS.register("upgrade_row", () -> new StorageUpgradeItem(new Item.Properties()));
    public static final DeferredItem<Item> UPGRADE_COLUMN_ITEM = ITEMS.register("upgrade_column", () -> new StorageUpgradeItem(new Item.Properties()));

    public static final DeferredHolder<MenuType<?>, MenuType<CompactChestScreenHandler>> COMPACT_CHEST_SCREEN_HANDLER = MENU_TYPES.register("compact_chest", () -> IMenuTypeExtension.create(CompactChestScreenHandler::new));
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> COMPACT_STORAGE_TAB = CREATIVE_MODE_TABS.register("compact_storage_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.compact_storage.general"))
        .icon(() -> new ItemStack(COMPACT_CHEST_BLOCKS[0].get(), 1))
        .displayItems((params, populator) -> {

            Arrays.stream(COMPACT_CHEST_BLOCKS).forEach(populator::accept);
            Arrays.stream(COMPACT_BARREL_BLOCKS).forEach(populator::accept);
            Arrays.stream(BACKPACK_ITEMS).forEach(populator::accept);
            Arrays.stream(DRUM_BLOCKS).forEach(populator::accept);

            populator.accept(UPGRADE_COLUMN_ITEM.get());
            populator.accept(UPGRADE_ROW_ITEM.get());
        }).build());

    static {
        for(int i = 0; i < 16; i++) {
            String dyeName = DyeColor.byId(i).getName().toLowerCase();
            DyeColor color = DyeColor.byId(i);
            final int id = i;

            COMPACT_CHEST_BLOCKS[i] = BLOCKS.register("compact_chest_" + dyeName, () ->
                    new NeoForgeCompactChestBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHEST).noOcclusion().strength(2f, 5f))
            );

            ITEMS.register("compact_chest_" + dyeName, () ->
                    new BlockItem(COMPACT_CHEST_BLOCKS[id].get(), new Item.Properties())
            );

            DYE_COLOR_TO_COMPACT_CHEST_MAP.put(color, COMPACT_CHEST_BLOCKS[i]);



            BACKPACK_ITEMS[i] = ITEMS.register("backpack_" + dyeName, () ->
                    new NeoForgeBackpackItem(new Item.Properties().stacksTo(1)));
            DYE_COLOR_TO_BACKPACK_MAP.put(color, BACKPACK_ITEMS[i]);



            COMPACT_BARREL_BLOCKS[i] = BLOCKS.register("compact_barrel_" + color, () ->
                    new NeoForgeCompactBarrelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).strength(2f, 5f)));
            DYE_COLOR_TO_COMPACT_BARREL_MAP.put(color, COMPACT_BARREL_BLOCKS[i]);

            ITEMS.register("compact_barrel_" + dyeName, () ->
                    new BlockItem(COMPACT_BARREL_BLOCKS[id].get(), new Item.Properties())
            );
        }

        for(int i = 0; i < CompactStorageUtil.DRUM_TYPES.length; i++) {
            final int id = i;

            DRUM_BLOCKS[id] = BLOCKS.register( CompactStorageUtil.DRUM_TYPES[id] + "_drum", () ->
                    new DrumBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL).strength(2f, 2f)));

            ITEMS.register(CompactStorageUtil.DRUM_TYPES[i] + "_drum", () -> new BlockItem(DRUM_BLOCKS[id].get(), new Item.Properties()));
        }
    }
    public CompactStorageNeoForge(IEventBus eventBus) {
        eventBus.register(this);

        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BLOCK_ENTITY_TYPES.register(eventBus);
        MENU_TYPES.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
    }

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, COMPACT_CHEST_ENTITY_TYPE.get(), (blockEntity, direction) -> {
            return new InvWrapper(blockEntity.getInventory());
        });
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, COMPACT_BARREL_ENTITY_TYPE.get(), (blockEntity, direction) -> {
            return new InvWrapper(blockEntity.getInventory());
        });
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, DRUM_ENTITY_TYPE.get(), (blockEntity, direction) -> {
            return new InvWrapper(blockEntity.inventory);
        });
    }
}
