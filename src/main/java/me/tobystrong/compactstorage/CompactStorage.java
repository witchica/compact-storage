package me.tobystrong.compactstorage;

import me.tobystrong.compactstorage.block.CompactChestBlock;
import me.tobystrong.compactstorage.block.entity.CompactChestBlockEntity;
import me.tobystrong.compactstorage.client.gui.CompactChestScreen;
import me.tobystrong.compactstorage.client.render.CompactChestBlockEntityRenderer;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import me.tobystrong.compactstorage.container.factory.CompactStorageContainerFactories;
import me.tobystrong.compactstorage.item.BackpackItem;
import me.tobystrong.compactstorage.item.ChestUpgradeItem;
import me.tobystrong.compactstorage.util.CompactStorageInventoryImpl;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CompactStorage.MOD_ID)
public class CompactStorage {
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "compact-storage";
    public static final String MOD_NAME = "CompactStorage";

    public static final ItemGroup COMPACT_STORAGE_ITEM_GROUP = new ItemGroup("compact-storage.general") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.CHEST, 1);
        }
    };

    public static final ResourceLocation COMPACT_CHEST_IDENTIFIER = new ResourceLocation(MOD_ID, "compact_chest");
    public static TileEntityType<CompactChestBlockEntity> COMPACT_CHEST_ENTITY_TYPE;
    public static final String COMPACT_CHEST_TRANSLATION_KEY = Util.makeTranslationKey("container", COMPACT_CHEST_IDENTIFIER);

    public static ContainerType<CompactChestContainer> COMPACT_CHEST_CONTAINER_TYPE;
    public static ContainerType<CompactChestContainer> BACKPACK_CONTAINER_TYPE;

    public static final ResourceLocation BACKPACK_IDENTIFIER = new ResourceLocation(MOD_ID, "backpack");
    public static final String BACKPACK_TRANSLATION_KEY = Util.makeTranslationKey("container", BACKPACK_IDENTIFIER);

    public static final Block COMPACT_CHEST = new CompactChestBlock(Block.Properties.from(Blocks.CHEST).notSolid().hardnessAndResistance(2f, 5f)).setRegistryName(COMPACT_CHEST_IDENTIFIER);
    public static final Item CHEST_UPGRADE_ROW = new ChestUpgradeItem(new Item.Properties().group(COMPACT_STORAGE_ITEM_GROUP)).setRegistryName(new ResourceLocation("compact-storage", "chest_upgrade_row"));
    public static final Item CHEST_UPGRADE_COLUMN = new ChestUpgradeItem(new Item.Properties().group(COMPACT_STORAGE_ITEM_GROUP)).setRegistryName(new ResourceLocation("compact-storage", "chest_upgrade_column"));
    public static final Item BACKPACK = new BackpackItem(new Item.Properties().group(COMPACT_STORAGE_ITEM_GROUP).maxStackSize(1)).setRegistryName(BACKPACK_IDENTIFIER);

    public CompactStorage() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        modBus.addListener(CompactStorage::registerBlocks);
        modBus.addListener(CompactStorage::registerItems);
        modBus.addListener(CompactStorage::registerTileEntities);
        modBus.addListener(CompactStorage::registerContainers);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            modBus.addListener(this::setupClient);
        });

        modBus.addListener(this::setup);
    }

    @OnlyIn(Dist.CLIENT)
    private void setupClient(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(COMPACT_CHEST_CONTAINER_TYPE, CompactChestScreen::new);
        ScreenManager.registerFactory(BACKPACK_CONTAINER_TYPE, CompactChestScreen::new);

        ClientRegistry.bindTileEntityRenderer(COMPACT_CHEST_ENTITY_TYPE, CompactChestBlockEntityRenderer::new);
    }

    private void setup(final FMLCommonSetupEvent event) {

    }

//    public static final Identifier BARREL_IDENTIFIER = new Identifier(MOD_ID, "barrel");
//    public static final Block BARREL = new BarrelBlock(Block.Settings.of(Material.METAL).nonOpaque().strength(2f, 1f));
//    public static BlockEntityType<BarrelBlockEntity> BARREL_ENTITY_TYPE;

//    @Override
//    public void onInitialize() {
//        Registry.register(Registry.BLOCK, COMPACT_CHEST_IDENTIFIER, COMPACT_CHEST);
//        Registry.register(Registry.ITEM, COMPACT_CHEST_IDENTIFIER, new BlockItem(COMPACT_CHEST, new Item.Settings().group(COMPACT_STORAGE_ITEM_GROUP)));
//
//        COMPACT_CHEST_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, COMPACT_CHEST_IDENTIFIER, BlockEntityType.Builder.create(CompactChestBlockEntity::new, COMPACT_CHEST).build(null));
//
//        ContainerProviderRegistry.INSTANCE.registerFactory(COMPACT_CHEST_IDENTIFIER, ((syncId, identifier, player, buf) -> {
//            final World world = player.world;
//            final BlockPos pos = buf.readBlockPos();
//
//            return world.getBlockState(pos).createContainerFactory(player.world, pos).createMenu(syncId, player.inventory, player);
//        }));
//
//        ContainerProviderRegistry.INSTANCE.registerFactory(BACKPACK_IDENTIFIER, ((syncId, identifier, player, buf) -> {
//            final World world = player.world;
//            final ItemStack stack = buf.readItemStack();
//            final Hand hand = buf.readInt() == 0 ? Hand.MAIN_HAND : Hand.OFF_HAND;
//            final CompactStorageInventoryImpl inventory = BackpackItem.getInventory(stack, hand, player);
//
//            return new CompactChestContainer(syncId, player.inventory, inventory.getInventory(), inventory.getInventoryWidth(), inventory.getInventoryHeight(), hand);
//        }));
//
//        Registry.register(Registry.ITEM, CHEST_UPGRADE_ROW_IDENTIFIER, CHEST_UPGRADE_ROW);
//        Registry.register(Registry.ITEM, CHEST_UPGRADE_COLUMN_IDENTIFIER, CHEST_UPGRADE_COLUMN);
//
//        Registry.register(Registry.ITEM, BACKPACK_IDENTIFIER, BACKPACK);
//
//        // Registry.register(Registry.BLOCK, BARREL_IDENTIFIER, BARREL);
//        // Registry.register(Registry.ITEM, BARREL_IDENTIFIER, new BlockItem(BARREL, new Item.Settings().group(COMPACT_STORAGE_ITEM_GROUP)));
//
//        // BARREL_ENTITY_TYPE = Registry.register(Registry.BLOCK_ENTITY_TYPE, BARREL_IDENTIFIER, BlockEntityType.Builder.create(BarrelBlockEntity::new, BARREL).build(null));
//    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        if(event.getRegistry().getRegistryName().getPath().equals("block")) {
            event.getRegistry().register(COMPACT_CHEST);
            log(Level.ALL, "Registering blocks");
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        if(event.getRegistry().getRegistryName().getPath().equals("item")) {
            event.getRegistry().registerAll(BACKPACK, CHEST_UPGRADE_COLUMN, CHEST_UPGRADE_ROW, new BlockItem(COMPACT_CHEST, new Item.Properties().group(COMPACT_STORAGE_ITEM_GROUP)).setRegistryName(COMPACT_CHEST_IDENTIFIER));

            log(Level.ALL, "Registering items");
        }
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        System.out.println(event.getRegistry().getRegistryName());
        if(event.getRegistry().getRegistryName().getPath().equals("block_entity_type")) {
            COMPACT_CHEST_ENTITY_TYPE = TileEntityType.Builder.create(CompactChestBlockEntity::new, COMPACT_CHEST).build(null);
            COMPACT_CHEST_ENTITY_TYPE.setRegistryName(COMPACT_CHEST_IDENTIFIER);
            event.getRegistry().registerAll(COMPACT_CHEST_ENTITY_TYPE);
        }
    }

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        System.out.println(event.getRegistry().getRegistryName());
        if(event.getRegistry().getRegistryName().getPath().equals("menu")) {
            BACKPACK_CONTAINER_TYPE = new ContainerType<CompactChestContainer>(new CompactStorageContainerFactories.BackpackContainerFactory());
            BACKPACK_CONTAINER_TYPE.setRegistryName(BACKPACK_IDENTIFIER);
            event.getRegistry().register(BACKPACK_CONTAINER_TYPE);

            COMPACT_CHEST_CONTAINER_TYPE = new ContainerType<CompactChestContainer>(new CompactStorageContainerFactories.ChestContainerFactory());
            COMPACT_CHEST_CONTAINER_TYPE.setRegistryName(COMPACT_CHEST_IDENTIFIER);
            event.getRegistry().register(COMPACT_CHEST_CONTAINER_TYPE);
        }
    }
}