package me.tobystrong.compactstorage;

import me.tobystrong.compactstorage.block.CompactBarrelBlock;
import me.tobystrong.compactstorage.block.CompactChestBlock;
import me.tobystrong.compactstorage.block.tile.CompactBarrelTileEntity;
import me.tobystrong.compactstorage.block.tile.CompactChestTileEntity;
import me.tobystrong.compactstorage.client.gui.CompactChestContainerScreen;
import me.tobystrong.compactstorage.client.renderer.CompactChestTileEntityRenderer;
import me.tobystrong.compactstorage.container.BackpackContainer;
import me.tobystrong.compactstorage.container.CompactBarrelContainer;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import me.tobystrong.compactstorage.item.BackpackItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("compactstorage")
public class CompactStorage
{
    private static final Logger LOGGER = LogManager.getLogger();

    //make a creative tab with a chest icon
    public static final ItemGroup compactStorageItemGroup = new ItemGroup("compact_storage") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(chest_blocks[0], 1);
        }
    };

    /*
        Variable storage in arrays for the coloured blocks, might change this later down the line
     */
    public static Block[] chest_blocks = new Block[DyeColor.values().length];
    public static Block[] barrel_blocks = new Block[DyeColor.values().length];

    public static TileEntityType<CompactChestTileEntity> COMPACT_CHEST_TILE_TYPE;
    public static TileEntityType<CompactBarrelTileEntity> COMPACT_BARREL_TILE_TYPE;

    public static ContainerType<CompactChestContainer> COMPACT_CHEST_CONTAINER_TYPE;
    public static ContainerType<BackpackContainer> BACKPACK_CONTAINER_TYPE;
    public static ContainerType<CompactBarrelContainer> BARREL_CONTAINER_TYPE;

    public static Item[] backpack_items = new Item[DyeColor.values().length];
    public static Item upgrade_row;
    public static Item upgrade_column;

    public CompactStorage() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);

        //create chest blocks and backpacks using the DyeColours class to conform to vanilla colours
        for(int i = 0; i < DyeColor.values().length; i++) {
            chest_blocks[i] = new CompactChestBlock().setRegistryName(new ResourceLocation("compactstorage", "compact_chest_" + DyeColor.values()[i].name().toLowerCase()));
        }

        for(int i = 0; i < DyeColor.values().length; i++) {
            barrel_blocks[i] = new CompactBarrelBlock().setRegistryName(new ResourceLocation("compactstorage", "compact_barrel_" + DyeColor.values()[i].name().toLowerCase()));
        }

        for(int i = 0; i < DyeColor.values().length; i++) {
            backpack_items[i] = new BackpackItem().setRegistryName(new ResourceLocation("compactstorage", "backpack_" + DyeColor.values()[i].name().toLowerCase()));
        }

        //upgrade items
        upgrade_column = new Item(new Item.Properties().maxStackSize(64).group(compactStorageItemGroup)).setRegistryName("compactstorage", "upgrade_column");
        upgrade_row = new Item(new Item.Properties().maxStackSize(64).group(compactStorageItemGroup)).setRegistryName("compactstorage", "upgrade_row");
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        //bind renderers and gui factories
        ClientRegistry.bindTileEntityRenderer(COMPACT_CHEST_TILE_TYPE, CompactChestTileEntityRenderer::new);

        ScreenManager.registerFactory(COMPACT_CHEST_CONTAINER_TYPE, CompactChestContainerScreen::new);
        ScreenManager.registerFactory(BACKPACK_CONTAINER_TYPE, CompactChestContainerScreen::new);
        ScreenManager.registerFactory(BARREL_CONTAINER_TYPE, CompactChestContainerScreen::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {

    }

    private void processIMC(final InterModProcessEvent event) {

    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            for(int i = 0; i < DyeColor.values().length; i++) {
                blockRegistryEvent.getRegistry().register(CompactStorage.chest_blocks[i]);
            }

            for(int i = 0; i < DyeColor.values().length; i++) {
                blockRegistryEvent.getRegistry().register(CompactStorage.barrel_blocks[i]);
            }
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            for(int i = 0; i < DyeColor.values().length; i++) {
                //register block items for the blocks, using our item group
                itemRegistryEvent.getRegistry().register(new BlockItem(CompactStorage.chest_blocks[i], new Item.Properties().group(compactStorageItemGroup)).setRegistryName(CompactStorage.chest_blocks[i].getRegistryName()));
                itemRegistryEvent.getRegistry().register(new BlockItem(CompactStorage.barrel_blocks[i], new Item.Properties().group(compactStorageItemGroup)).setRegistryName(CompactStorage.barrel_blocks[i].getRegistryName()));
            }

            itemRegistryEvent.getRegistry().registerAll(backpack_items);
            itemRegistryEvent.getRegistry().registerAll(upgrade_column, upgrade_row);
        }

        @SubscribeEvent
        public static void onTileEntityTypeRegistry(final RegistryEvent.Register<TileEntityType<?>> tileTypeRegistryEvent) {
            //create tile type for compact chest
            COMPACT_CHEST_TILE_TYPE = TileEntityType.Builder.create(CompactChestTileEntity::new, chest_blocks).build(null);
            COMPACT_CHEST_TILE_TYPE.setRegistryName("compactstorage", "compact_chest");
            tileTypeRegistryEvent.getRegistry().register(COMPACT_CHEST_TILE_TYPE);

            COMPACT_BARREL_TILE_TYPE = TileEntityType.Builder.create(CompactBarrelTileEntity::new, barrel_blocks).build(null);
            COMPACT_BARREL_TILE_TYPE.setRegistryName("compactstorage", "compact_barrel");
            tileTypeRegistryEvent.getRegistry().register(COMPACT_BARREL_TILE_TYPE);
        }

        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> containerTypeRegister) {
            //create container types, linking to the client size creation
            COMPACT_CHEST_CONTAINER_TYPE = IForgeContainerType.create(CompactChestContainer::createContainerClientSide);
            COMPACT_CHEST_CONTAINER_TYPE.setRegistryName("compactstorage", "compact_chest");

            BACKPACK_CONTAINER_TYPE = IForgeContainerType.create(BackpackContainer::createContainerFromItemstack);
            BACKPACK_CONTAINER_TYPE.setRegistryName("compactstorage", "backpack");

            BARREL_CONTAINER_TYPE = IForgeContainerType.create(CompactBarrelContainer::createContainerClientSide);
            BARREL_CONTAINER_TYPE.setRegistryName("compactstorage", "compact_barrel");

            containerTypeRegister.getRegistry().registerAll(COMPACT_CHEST_CONTAINER_TYPE, BACKPACK_CONTAINER_TYPE, BARREL_CONTAINER_TYPE);
        }
    }
}
