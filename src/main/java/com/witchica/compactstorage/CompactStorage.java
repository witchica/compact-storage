package com.witchica.compactstorage;

import com.witchica.compactstorage.block.CompactBarrelBlock;
import com.witchica.compactstorage.block.DrumBlock;
import com.witchica.compactstorage.block.entity.CompactBarrelBlockEntity;
import com.witchica.compactstorage.block.entity.DrumBlockEntity;
import com.witchica.compactstorage.item.BackpackItem;
import com.witchica.compactstorage.item.StorageUpgradeItem;
import com.witchica.compactstorage.util.CompactStorageUtil;
import com.witchica.compactstorage.util.RegistryHolder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import com.witchica.compactstorage.block.CompactChestBlock;
import com.witchica.compactstorage.block.entity.CompactChestBlockEntity;
import com.witchica.compactstorage.screen.CompactChestScreenHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CompactStorage implements ModInitializer {

    public static final String MOD_ID = "compact_storage";
    public static final String MOD_NAME = "CompactStorage";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/**
	 * Blocks
	 */
	public static Identifier COMPACT_CHEST_GENERIC_IDENTIFIER = new Identifier(MOD_ID, "compact_chest");
	public static Identifier COMPACT_BARREL_GENERIC_IDENTIFIER = new Identifier(MOD_ID, "compact_barrel");
	public static BlockEntityType<CompactChestBlockEntity> COMPACT_CHEST_ENTITY_TYPE;
	public static BlockEntityType<CompactBarrelBlockEntity> COMPACT_BARREL_ENTITY_TYPE;
    public static final String COMPACT_CHEST_TRANSLATION_KEY = Util.createTranslationKey("container", COMPACT_CHEST_GENERIC_IDENTIFIER);
	public static final ExtendedScreenHandlerType<CompactChestScreenHandler> COMPACT_CHEST_SCREEN_HANDLER = new  ExtendedScreenHandlerType<CompactChestScreenHandler>(CompactChestScreenHandler::new);


	public static final RegistryHolder.Blocks[] COMPACT_CHEST_BLOCKS = new RegistryHolder.Blocks[16];
	public static final RegistryHolder.Blocks[] COMPACT_BARREL_BLOCKS = new RegistryHolder.Blocks[16];
	public static final RegistryHolder.Blocks[] DRUM_BLOCKS = new RegistryHolder.Blocks[CompactStorageUtil.DRUM_TYPES.length];
	public static final RegistryHolder.Items[] BACKPACK_ITEMS = new RegistryHolder.Items[16];

	public static RegistryHolder<BlockEntityType<DrumBlockEntity>> DRUM_BLOCK_ENTITY_TYPE;

	public static final HashMap<DyeColor, RegistryHolder.Blocks> DYE_COLOR_TO_COMPACT_CHEST_MAP = new HashMap<DyeColor, RegistryHolder.Blocks>();
	public static final HashMap<DyeColor, RegistryHolder.Blocks> DYE_COLOR_TO_COMPACT_BARREL_MAP = new HashMap<DyeColor, RegistryHolder.Blocks>();

	/**
	 * Items
	 */

	public static final RegistryHolder.Items UPGRADE_ROW_ITEM = new RegistryHolder.Items("upgrade_row", new StorageUpgradeItem(new FabricItemSettings()));
	public static final RegistryHolder.Items UPGRADE_COLUMN_ITEM = new RegistryHolder.Items("upgrade_column", new StorageUpgradeItem(new FabricItemSettings()));

	public static final HashMap<DyeColor, RegistryHolder.Items> DYE_COLOR_TO_BACKPACK_MAP = new HashMap<DyeColor, RegistryHolder.Items>();

	static {
		for(int i = 0; i < 16; i++) {
			String dyeName = DyeColor.byId(i).getName().toLowerCase();

			COMPACT_CHEST_BLOCKS[i] = new RegistryHolder.Blocks("compact_chest_" + dyeName, new CompactChestBlock(Block.Settings.copy(Blocks.CHEST).nonOpaque().strength(2f, 5f)));
			DYE_COLOR_TO_COMPACT_CHEST_MAP.put(DyeColor.byId(i), COMPACT_CHEST_BLOCKS[i]);

			BACKPACK_ITEMS[i] = new RegistryHolder.Items("backpack_" + dyeName, new BackpackItem(new FabricItemSettings().maxCount(1)));
			DYE_COLOR_TO_BACKPACK_MAP.put(DyeColor.byId(i), BACKPACK_ITEMS[i]);

			COMPACT_BARREL_BLOCKS[i] = new RegistryHolder.Blocks("compact_barrel_" + dyeName, new CompactBarrelBlock(AbstractBlock.Settings.copy(Blocks.BARREL).strength(2f, 5f)));
			DYE_COLOR_TO_COMPACT_BARREL_MAP.put(DyeColor.byId(i), COMPACT_BARREL_BLOCKS[i]);
		}

		for(int i = 0; i < CompactStorageUtil.DRUM_TYPES.length; i++) {
			DRUM_BLOCKS[i] = new RegistryHolder.Blocks(CompactStorageUtil.DRUM_TYPES[i] + "_drum", new DrumBlock(Block.Settings.copy(Blocks.BARREL)));
		}

	}

	public static final ItemGroup COMPACT_STORAGE_ITEM_GROUP = FabricItemGroup.builder()
			.displayName(Text.translatable("itemGroup.compact_storage.general"))
			.icon(() -> new ItemStack(COMPACT_CHEST_BLOCKS[0].get(), 1))
			.entries(((displayContext, entries) -> {
				entries.addAll(Arrays.stream(COMPACT_CHEST_BLOCKS).map((block) -> new ItemStack(block.get(), 1)).toList());
				entries.addAll(Arrays.stream(COMPACT_BARREL_BLOCKS).map((block) -> new ItemStack(block.get(), 1)).toList());
				entries.addAll(Arrays.stream(DRUM_BLOCKS).map((block) -> new ItemStack(block.get(), 1)).toList());
				entries.addAll(Arrays.stream(BACKPACK_ITEMS).map((block) -> new ItemStack(block.get(), 1)).toList());
				entries.add(UPGRADE_COLUMN_ITEM.get());
				entries.add(UPGRADE_ROW_ITEM.get());
			}))
			.build();

	@Override
	public void onInitialize() {
		LOGGER.info("Welcome to Compact Storage!");

		for (int i = 0; i < 16; i++) {
			COMPACT_CHEST_BLOCKS[i].registerBlockAndItem();
			COMPACT_BARREL_BLOCKS[i].registerBlockAndItem();
			BACKPACK_ITEMS[i].register(Registries.ITEM);
		}

		Arrays.stream(DRUM_BLOCKS).forEach(RegistryHolder.Blocks::registerBlockAndItem);

		Registry.register(Registries.SCREEN_HANDLER, COMPACT_CHEST_GENERIC_IDENTIFIER, COMPACT_CHEST_SCREEN_HANDLER);
		COMPACT_CHEST_ENTITY_TYPE = Registry.register(Registries.BLOCK_ENTITY_TYPE, COMPACT_CHEST_GENERIC_IDENTIFIER, FabricBlockEntityTypeBuilder.create(CompactChestBlockEntity::new, Arrays.stream(COMPACT_CHEST_BLOCKS).map(RegistryHolder::get).toArray(Block[]::new)).build(null));
		COMPACT_BARREL_ENTITY_TYPE = Registry.register(Registries.BLOCK_ENTITY_TYPE, COMPACT_BARREL_GENERIC_IDENTIFIER, FabricBlockEntityTypeBuilder.create(CompactBarrelBlockEntity::new, Arrays.stream(COMPACT_BARREL_BLOCKS).map(RegistryHolder::get).toArray(Block[]::new)).build(null));

		DRUM_BLOCK_ENTITY_TYPE = new RegistryHolder<BlockEntityType<DrumBlockEntity>>("drum", FabricBlockEntityTypeBuilder.create(DrumBlockEntity::new, Arrays.stream(DRUM_BLOCKS).map(RegistryHolder::get).toArray(Block[]::new)).build(null));
		DRUM_BLOCK_ENTITY_TYPE.register(Registries.BLOCK_ENTITY_TYPE);

		UPGRADE_ROW_ITEM.register(Registries.ITEM);
		UPGRADE_COLUMN_ITEM.register(Registries.ITEM);

		Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "general"), COMPACT_STORAGE_ITEM_GROUP);

		ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.inventoryStorage, COMPACT_CHEST_ENTITY_TYPE);
		ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.inventoryStorage, COMPACT_BARREL_ENTITY_TYPE);

		ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.inventoryWrapper, DRUM_BLOCK_ENTITY_TYPE.get());
	}
}
