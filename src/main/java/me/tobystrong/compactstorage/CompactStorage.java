package me.tobystrong.compactstorage;

import me.tobystrong.compactstorage.block.BarrelBlock;
import me.tobystrong.compactstorage.block.CompactChestBlock;
import me.tobystrong.compactstorage.block.entity.BarrelBlockEntity;
import me.tobystrong.compactstorage.block.entity.CompactChestBlockEntity;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CompactStorage implements ModInitializer {
    
    /*
    Changes in this version
     - All blocks will have standard crafting recipes and will be upgradable in the GUI of the block
     - Upgrades will take 1 material rather than 4 like before, calculated from the increase in size
     - Total rewrite yay
     */
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "compact-storage";
    public static final String MOD_NAME = "CompactStorage";

    public static final ItemGroup COMPACT_STORAGE_ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "general"), () -> new ItemStack(Blocks.CHEST, 1));

    public static final Block COMPACT_CHEST = new CompactChestBlock(Block.Settings.copy(Blocks.CHEST));
    public static final Identifier COMPACT_CHEST_IDENTIFIER = new Identifier(MOD_ID, "compact_chest");
    public static BlockEntityType<CompactChestBlockEntity> COMPACT_CHEST_ENTITY_TYPE;
    public static final String COMPACT_CHEST_TRANSLATION_KEY = Util.createTranslationKey("container", COMPACT_CHEST_IDENTIFIER);

    public static final Item CHEST_UPGRADE_ROW = new Item(new Item.Settings().group(COMPACT_STORAGE_ITEM_GROUP));
    public static final Identifier CHEST_UPGRADE_ROW_IDENTIFIER = new Identifier(MOD_ID, "chest_upgrade_row");

    public static final Item CHEST_UPGRADE_COLUMN = new Item(new Item.Settings().group(COMPACT_STORAGE_ITEM_GROUP));
    public static final Identifier CHEST_UPGRADE_COLUMN_IDENTIFIER = new Identifier(MOD_ID, "chest_upgrade_column");

    public static final Identifier BARREL_IDENTIFIER = new Identifier(MOD_ID, "barrel");
    public static final Block BARREL = new BarrelBlock(Block.Settings.of(Material.METAL).nonOpaque().strength(2f, 1f));
    public static BlockEntityType<BarrelBlockEntity> BARREL_ENTITY_TYPE;

    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, COMPACT_CHEST_IDENTIFIER, COMPACT_CHEST);
        Registry.register(Registry.ITEM, COMPACT_CHEST_IDENTIFIER, new BlockItem(COMPACT_CHEST, new Item.Settings().group(COMPACT_STORAGE_ITEM_GROUP)));
        
        COMPACT_CHEST_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, COMPACT_CHEST_IDENTIFIER, BlockEntityType.Builder.create(CompactChestBlockEntity::new, COMPACT_CHEST).build(null));

        ContainerProviderRegistry.INSTANCE.registerFactory(COMPACT_CHEST_IDENTIFIER, ((syncId, identifier, player, buf) -> {
            final World world = player.world;
            final BlockPos pos = buf.readBlockPos();

            return world.getBlockState(pos).createContainerFactory(player.world, pos).createMenu(syncId, player.inventory, player);
        }));

        Registry.register(Registry.ITEM, CHEST_UPGRADE_ROW_IDENTIFIER, CHEST_UPGRADE_ROW);
        Registry.register(Registry.ITEM, CHEST_UPGRADE_COLUMN_IDENTIFIER, CHEST_UPGRADE_COLUMN);

        Registry.register(Registry.BLOCK, BARREL_IDENTIFIER, BARREL);
        Registry.register(Registry.ITEM, BARREL_IDENTIFIER, new BlockItem(BARREL, new Item.Settings().group(COMPACT_STORAGE_ITEM_GROUP)));

        BARREL_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, BARREL_IDENTIFIER, BlockEntityType.Builder.create(BarrelBlockEntity::new, BARREL).build(null));
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}