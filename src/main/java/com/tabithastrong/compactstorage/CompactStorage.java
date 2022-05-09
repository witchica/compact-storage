package com.tabithastrong.compactstorage;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
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

public class CompactStorage implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");

    public static final String MOD_ID = "compact-storage";
    public static final String MOD_NAME = "CompactStorage";

	/**
	 * Blocks
	 */
	
    public static final Block COMPACT_CHEST = new CompactChestBlock(Block.Settings.copy(Blocks.CHEST).nonOpaque().strength(2f, 5f));
    public static final Identifier COMPACT_CHEST_IDENTIFIER = new Identifier(MOD_ID, "compact_chest");
    public static BlockEntityType<CompactChestBlockEntity> COMPACT_CHEST_ENTITY_TYPE;
    public static final String COMPACT_CHEST_TRANSLATION_KEY = Util.createTranslationKey("container", COMPACT_CHEST_IDENTIFIER);
	public static final ExtendedScreenHandlerType<CompactChestScreenHandler> COMPACT_CHEST_SCREEN_HANDLER = new  ExtendedScreenHandlerType<CompactChestScreenHandler>(CompactChestScreenHandler::new);

	public static final ItemGroup COMPACT_STORAGE_ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "general"), () -> {
		return new ItemStack(Blocks.CHEST);
	});

	@Override
	public void onInitialize() {
		LOGGER.info("Welcome to Compact Storage!");

		Registry.register(Registry.BLOCK, COMPACT_CHEST_IDENTIFIER, COMPACT_CHEST);
		Registry.register(Registry.ITEM, COMPACT_CHEST_IDENTIFIER, new BlockItem(COMPACT_CHEST, new FabricItemSettings().group(COMPACT_STORAGE_ITEM_GROUP)));
		Registry.register(Registry.SCREEN_HANDLER, COMPACT_CHEST_IDENTIFIER, COMPACT_CHEST_SCREEN_HANDLER);

		COMPACT_CHEST_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, COMPACT_CHEST_IDENTIFIER, FabricBlockEntityTypeBuilder.create(CompactChestBlockEntity::new, COMPACT_CHEST).build(null));

	}
}
