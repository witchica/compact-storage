package com.tabithastrong.compactstorage;

import com.tabithastrong.compactstorage.item.BackpackItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import com.tabithastrong.compactstorage.block.CompactChestBlock;
import com.tabithastrong.compactstorage.block.entity.CompactChestBlockEntity;
import com.tabithastrong.compactstorage.screen.CompactChestScreenHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CompactStorage implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("compact_storage");

    public static final String MOD_ID = "compact_storage";
    public static final String MOD_NAME = "CompactStorage";

	/**
	 * Blocks
	 */
	public static Identifier COMPACT_CHEST_GENERIC_IDENTIFIER = new Identifier(MOD_ID, "compact_chest");
    public static BlockEntityType<CompactChestBlockEntity> COMPACT_CHEST_ENTITY_TYPE;
    public static final String COMPACT_CHEST_TRANSLATION_KEY = Util.createTranslationKey("container", COMPACT_CHEST_GENERIC_IDENTIFIER);
	public static final ExtendedScreenHandlerType<CompactChestScreenHandler> COMPACT_CHEST_SCREEN_HANDLER = new  ExtendedScreenHandlerType<CompactChestScreenHandler>(CompactChestScreenHandler::new);

	public static final Block[] COMPACT_CHEST_BLOCKS = new Block[16];
	public static final Identifier[] COMPACT_CHEST_IDENTIFIERS = new Identifier[16];

	public static final HashMap<DyeColor, Block> DYE_COLOR_TO_COMPACT_CHEST_MAP = new HashMap<DyeColor, Block>();

	public static final ItemGroup COMPACT_STORAGE_ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "general"), () -> {
		return new ItemStack(COMPACT_CHEST_BLOCKS[1], 1);
	});

	/**
	 * Items
	 */

	public static final Item UPGRADE_ROW_ITEM = new Item(new FabricItemSettings().group(COMPACT_STORAGE_ITEM_GROUP));
	public static final Identifier UPGRADE_ROW_ITEM_IDENTIFIER = new Identifier(MOD_ID, "upgrade_row");
	public static final Item UPGRADE_COLUMN_ITEM = new Item(new FabricItemSettings().group(COMPACT_STORAGE_ITEM_GROUP));
	public static final Identifier UPGRADE_COLUMN_ITEM_IDENTIFIER = new Identifier(MOD_ID, "upgrade_column");

	public static Identifier BACKPACK_GENERIC_IDENTIFIER = new Identifier(MOD_ID, "backpack");
	public static final Item[] BACKPACK_ITEMS = new Item[16];
	public static final Identifier[] BACKPACK_ITEM_IDENTIFIERS = new Identifier[16];
	public static final HashMap<DyeColor, Item> DYE_COLOR_TO_BACKPACK_MAP = new HashMap<DyeColor, Item>();

	static {
		for(int i = 0; i < 16; i++) {
			String dyeName = DyeColor.byId(i).getName().toLowerCase();

			COMPACT_CHEST_BLOCKS[i] = new CompactChestBlock(Block.Settings.copy(Blocks.CHEST).nonOpaque().strength(2f, 5f));
			COMPACT_CHEST_IDENTIFIERS[i] = new Identifier(MOD_ID, "compact_chest_" + dyeName);
			DYE_COLOR_TO_COMPACT_CHEST_MAP.put(DyeColor.byId(i), COMPACT_CHEST_BLOCKS[i]);

			BACKPACK_ITEMS[i] = new BackpackItem(new FabricItemSettings().maxCount(1).group(COMPACT_STORAGE_ITEM_GROUP));
			BACKPACK_ITEM_IDENTIFIERS[i] = new Identifier(MOD_ID, "backpack_" + dyeName);
			DYE_COLOR_TO_BACKPACK_MAP.put(DyeColor.byId(i), BACKPACK_ITEMS[i]);
		}
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Welcome to Compact Storage!");

		for(int i = 0; i < 16; i++) {
			Registry.register(Registry.BLOCK, COMPACT_CHEST_IDENTIFIERS[i], COMPACT_CHEST_BLOCKS[i]);
			Registry.register(Registry.ITEM, COMPACT_CHEST_IDENTIFIERS[i], new BlockItem(COMPACT_CHEST_BLOCKS[i], new FabricItemSettings().group(COMPACT_STORAGE_ITEM_GROUP)));

			Registry.register(Registry.ITEM, BACKPACK_ITEM_IDENTIFIERS[i], BACKPACK_ITEMS[i]);
		}

		Registry.register(Registry.SCREEN_HANDLER, COMPACT_CHEST_GENERIC_IDENTIFIER, COMPACT_CHEST_SCREEN_HANDLER);
		COMPACT_CHEST_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, COMPACT_CHEST_GENERIC_IDENTIFIER, FabricBlockEntityTypeBuilder.create(CompactChestBlockEntity::new, COMPACT_CHEST_BLOCKS).build(null));

		Registry.register(Registry.ITEM, UPGRADE_ROW_ITEM_IDENTIFIER, UPGRADE_ROW_ITEM);
		Registry.register(Registry.ITEM, UPGRADE_COLUMN_ITEM_IDENTIFIER, UPGRADE_COLUMN_ITEM);
	}
}
