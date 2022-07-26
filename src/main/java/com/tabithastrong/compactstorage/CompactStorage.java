package com.tabithastrong.compactstorage;

import com.mojang.logging.LogUtils;
import com.tabithastrong.compactstorage.block.CompactBarrelBlock;
import com.tabithastrong.compactstorage.block.CompactChestBlock;
import com.tabithastrong.compactstorage.block.entity.CompactBarrelBlockEntity;
import com.tabithastrong.compactstorage.block.entity.CompactChestBlockEntity;
import com.tabithastrong.compactstorage.item.BackpackItem;
import com.tabithastrong.compactstorage.item.StorageUpgradeItem;
import com.tabithastrong.compactstorage.screen.CompactChestScreenHandler;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.awt.*;
import java.util.HashMap;
import java.util.stream.Collectors;

@Mod("compact_storage")
public class CompactStorage
{
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final String MOD_ID = "compact_storage";
    public static final String MOD_NAME = "CompactStorage";

    /**
     * Deferred Registries
     */
    public static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);

    /**
     * Blocks
     */
    public static ResourceLocation COMPACT_CHEST_GENERIC_IDENTIFIER = new ResourceLocation(MOD_ID, "compact_chest");
    public static ResourceLocation COMPACT_BARREL_GENERIC_IDENTIFIER = new ResourceLocation(MOD_ID, "compact_barrel");

    public static final RegistryObject<Block>[] COMPACT_CHEST_BLOCKS = new RegistryObject[16];
    public static final RegistryObject<Block>[] COMPACT_BARREL_BLOCKS = new RegistryObject[16];

    public static Block[] mapRegistryObjectToBlocks(RegistryObject<Block>[] array) {
        Block[] mapped = new Block[array.length];

        for(int i = 0; i < mapped.length; i++) {
            mapped[i] = array[i].get();
        }

        return mapped;
    }

    public static RegistryObject<BlockEntityType<CompactBarrelBlockEntity> > COMPACT_BARREL_ENTITY_TYPE =
            BLOCK_ENTITY_TYPE_REGISTER.register("compact_barrel", () -> BlockEntityType.Builder.of(CompactBarrelBlockEntity::new, mapRegistryObjectToBlocks(COMPACT_BARREL_BLOCKS)).build(null));

    public static RegistryObject<BlockEntityType<CompactChestBlockEntity> > COMPACT_CHEST_ENTITY_TYPE =
            BLOCK_ENTITY_TYPE_REGISTER.register("compact_chest", () -> BlockEntityType.Builder.of(CompactChestBlockEntity::new, mapRegistryObjectToBlocks(COMPACT_CHEST_BLOCKS)).build(null));

    public static final String COMPACT_CHEST_TRANSLATION_KEY = Util.makeDescriptionId("container", COMPACT_CHEST_GENERIC_IDENTIFIER);

    public static final RegistryObject<MenuType<CompactChestScreenHandler>> COMPACT_CHEST_SCREEN_HANDLER = MENU_TYPE_REGISTER.register("compact_chest", () -> new MenuType<CompactChestScreenHandler>(new IContainerFactory<CompactChestScreenHandler>() {
        @Override
        public CompactChestScreenHandler create(int windowId, Inventory inv, FriendlyByteBuf data) {
            return new CompactChestScreenHandler(windowId, inv, data);
        }
    }));


    public static final HashMap<DyeColor, RegistryObject<Block>> DYE_COLOR_TO_COMPACT_CHEST_MAP = new HashMap<DyeColor, RegistryObject<Block>>();


    public static final HashMap<DyeColor, RegistryObject<Block>> DYE_COLOR_TO_COMPACT_BARREL_MAP = new HashMap<DyeColor, RegistryObject<Block>>();

    public static final CreativeModeTab COMPACT_STORAGE_ITEM_GROUP = new CreativeModeTab("compact_storage.general") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(COMPACT_CHEST_BLOCKS[1].get(), 1);
        }
    };
    /**
     * Items
     */
    public static final RegistryObject<Item> UPGRADE_ROW_ITEM = ITEM_REGISTER.register("upgrade_row", () -> new StorageUpgradeItem(new Item.Properties().tab(COMPACT_STORAGE_ITEM_GROUP)));
    public static final RegistryObject<Item> UPGRADE_COLUMN_ITEM = ITEM_REGISTER.register("upgrade_column", () -> new StorageUpgradeItem(new Item.Properties().tab(COMPACT_STORAGE_ITEM_GROUP)));

    public static ResourceLocation BACKPACK_GENERIC_IDENTIFIER = new ResourceLocation(MOD_ID, "backpack");
    public static final RegistryObject<Item>[] BACKPACK_ITEMS = new RegistryObject[16];
    public static final HashMap<DyeColor, RegistryObject<Item>> DYE_COLOR_TO_BACKPACK_MAP = new HashMap<DyeColor, RegistryObject<Item>>();

    static {
        for(int i = 0; i < 16; i++) {
            String dyeName = DyeColor.byId(i).getName().toLowerCase();
            DyeColor color = DyeColor.byId(i);
            final int id = i;

            COMPACT_CHEST_BLOCKS[i] = BLOCK_REGISTER.register("compact_chest_" + dyeName, () ->
                    new CompactChestBlock(BlockBehaviour.Properties.copy(Blocks.CHEST).noOcclusion().strength(2f, 5f))
            );

            ITEM_REGISTER.register("compact_chest_" + dyeName, () ->
                new BlockItem(COMPACT_CHEST_BLOCKS[id].get(), new Item.Properties().tab(COMPACT_STORAGE_ITEM_GROUP))
            );

            DYE_COLOR_TO_COMPACT_CHEST_MAP.put(color, COMPACT_CHEST_BLOCKS[i]);



            BACKPACK_ITEMS[i] = ITEM_REGISTER.register("backpack_" + dyeName, () ->
                    new BackpackItem(new Item.Properties().stacksTo(1).tab(COMPACT_STORAGE_ITEM_GROUP)));
            DYE_COLOR_TO_BACKPACK_MAP.put(color, BACKPACK_ITEMS[i]);



            COMPACT_BARREL_BLOCKS[i] = BLOCK_REGISTER.register("compact_barrel_" + color, () ->
                    new CompactBarrelBlock(BlockBehaviour.Properties.copy(Blocks.BARREL).strength(2f, 5f)));
            DYE_COLOR_TO_COMPACT_BARREL_MAP.put(color, COMPACT_BARREL_BLOCKS[i]);

            ITEM_REGISTER.register("compact_barrel_" + dyeName, () ->
                    new BlockItem(COMPACT_BARREL_BLOCKS[id].get(), new Item.Properties().tab(COMPACT_STORAGE_ITEM_GROUP))
            );
        }
    }

    public CompactStorage()
    {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);

        BLOCK_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEM_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCK_ENTITY_TYPE_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        MENU_TYPE_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

    private void setup(final FMLCommonSetupEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }
}
