package me.tobystrong.compactstorage;

import me.tobystrong.compactstorage.block.CompactChestBlock;
import me.tobystrong.compactstorage.block.tile.CompactChestTileEntity;
import me.tobystrong.compactstorage.client.gui.CompactChestContainerScreen;
import me.tobystrong.compactstorage.client.renderer.CompactChestTileEntityRenderer;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import me.tobystrong.compactstorage.item.BackpackItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
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

import java.util.stream.Collectors;

@Mod("compactstorage")
public class CompactStorage
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static final ItemGroup compactStorageItemGroup = new ItemGroup("compact_storage") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.CHEST, 1);
        }
    };

    public static Item[] backpackItems = new Item[DyeColor.values().length];
    public static Block[] chestBlocks = new Block[DyeColor.values().length];
    public static TileEntityType<CompactChestTileEntity> COMPACT_CHEST_TILE_TYPE;
    public static ContainerType<CompactChestContainer> COMPACT_CHEST_CONTAINER_TYPE;

    public CompactStorage() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);

        for(int i = 0; i < DyeColor.values().length; i++) {
            chestBlocks[i] = new CompactChestBlock().setRegistryName(new ResourceLocation("compactstorage", "compact_chest_" + DyeColor.values()[i].name().toLowerCase()));
        }

        for(int i = 0; i < DyeColor.values().length; i++) {
            backpackItems[i] = new BackpackItem().setRegistryName(new ResourceLocation("compactstorage", "backpack_" + DyeColor.values()[i].name().toLowerCase()));
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(COMPACT_CHEST_TILE_TYPE, CompactChestTileEntityRenderer::new);
        ScreenManager.registerFactory(COMPACT_CHEST_CONTAINER_TYPE, CompactChestContainerScreen::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {

    }

    private void processIMC(final InterModProcessEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            for(int i = 0; i < DyeColor.values().length; i++) {
                blockRegistryEvent.getRegistry().register(CompactStorage.chestBlocks[i]);
            }
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            for(int i = 0; i < DyeColor.values().length; i++) {
                itemRegistryEvent.getRegistry().register(new BlockItem(CompactStorage.chestBlocks[i], new Item.Properties().group(compactStorageItemGroup)).setRegistryName(CompactStorage.chestBlocks[i].getRegistryName()));
            }

            itemRegistryEvent.getRegistry().registerAll(backpackItems);
        }

        @SubscribeEvent
        public static void onTileEntityTypeRegistry(final RegistryEvent.Register<TileEntityType<?>> tileTypeRegistryEvent) {
            COMPACT_CHEST_TILE_TYPE = TileEntityType.Builder.create(CompactChestTileEntity::new, chestBlocks).build(null);
            COMPACT_CHEST_TILE_TYPE.setRegistryName("compactstorage", "compact_chest");
            tileTypeRegistryEvent.getRegistry().register(COMPACT_CHEST_TILE_TYPE);
        }

        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> containerTypeRegister) {
            COMPACT_CHEST_CONTAINER_TYPE = IForgeContainerType.create(CompactChestContainer::createContainerClientSide);
            COMPACT_CHEST_CONTAINER_TYPE.setRegistryName("compactstorage", "compact_chest");
            containerTypeRegister.getRegistry().registerAll(COMPACT_CHEST_CONTAINER_TYPE);
        }
    }
}
