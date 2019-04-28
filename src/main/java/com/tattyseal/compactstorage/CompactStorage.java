package com.tattyseal.compactstorage;

import java.awt.Color;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tattyseal.compactstorage.block.BlockBarrel;
import com.tattyseal.compactstorage.block.BlockChest;
import com.tattyseal.compactstorage.block.BlockChestBuilder;
import com.tattyseal.compactstorage.block.BlockFluidBarrel;
import com.tattyseal.compactstorage.creativetabs.CreativeTabCompactStorage;
import com.tattyseal.compactstorage.event.CompactStorageEventHandler;
import com.tattyseal.compactstorage.event.ConnectionHandler;
import com.tattyseal.compactstorage.item.ItemBackpack;
import com.tattyseal.compactstorage.item.ItemBlockChest;
import com.tattyseal.compactstorage.packet.MessageCraftChest;
import com.tattyseal.compactstorage.packet.MessageUpdateBuilder;
import com.tattyseal.compactstorage.proxy.IProxy;
import com.tattyseal.compactstorage.tileentity.TileEntityBarrel;
import com.tattyseal.compactstorage.tileentity.TileEntityBarrelFluid;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;
import com.tattyseal.compactstorage.tileentity.TileEntityChestBuilder;
import com.tattyseal.compactstorage.util.StorageInfo;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by Toby on 06/11/2014.
 * Updated for 3.0 on the 16/02/2018
 */
@Mod(modid = CompactStorage.MODID, name = CompactStorage.NAME, version = CompactStorage.VERSION, guiFactory = "com.tattyseal.compactstorage.client.gui.factory.CompactStorageGuiFactory")
public class CompactStorage {

	public static final String MODID = "compactstorage";
	public static final String NAME = "Compact Storage";
	public static final String VERSION = "4.0.0";
	public static final CreativeTabs TAB = new CreativeTabCompactStorage();
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	@Instance
	public static CompactStorage instance;

	@SidedProxy(clientSide = "com.tattyseal.compactstorage.proxy.ClientProxy", serverSide = "com.tattyseal.compactstorage.proxy.ServerProxy", modId = CompactStorage.MODID)
	public static IProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		OreDictionary.registerOre("barsIron", Blocks.IRON_BARS);
		OreDictionary.registerOre("blockChest", Blocks.CHEST);
		OreDictionary.registerOre("itemClay", Items.CLAY_BALL);

		OreDictionary.registerOre("string", Items.STRING);
		OreDictionary.registerOre("wool", Blocks.WOOL);

		NETWORK.registerMessage(MessageUpdateBuilder::onMessage, MessageUpdateBuilder.class, 0, Side.SERVER);
		NETWORK.registerMessage(MessageCraftChest::onMessage, MessageCraftChest.class, 1, Side.SERVER);

		ConfigurationHandler.configFile = event.getSuggestedConfigurationFile();

		MinecraftForge.EVENT_BUS.register(new ConnectionHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRenderers();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(new CompactStorageEventHandler());

		GameRegistry.addShapedRecipe(new ResourceLocation("compactstorage", "chest_builder"), null, new ItemStack(CompactRegistry.CHEST_BUILDER, 1), "ILI", "ICI", "ILI", 'I', new ItemStack(Items.IRON_INGOT, 1), 'C', new ItemStack(Blocks.CHEST, 1), 'L', new ItemStack(Blocks.LEVER, 1));
		GameRegistry.addShapedRecipe(new ResourceLocation("compactstorage", "barrel"), null, new ItemStack(CompactRegistry.BARREL, 1), "III", "GCG", "III", 'I', new ItemStack(Items.IRON_INGOT, 1), 'G', new ItemStack(Blocks.IRON_BLOCK, 1), 'C', new ItemStack(Blocks.CHEST, 1));
		GameRegistry.addShapedRecipe(new ResourceLocation("compactstorage", "drum"), null, new ItemStack(CompactRegistry.FLUID_BARREL, 1), "ICI", "GIG", "ICI", 'I', new ItemStack(Items.IRON_INGOT, 1), 'G', new ItemStack(Blocks.IRON_BLOCK, 1), 'C', new ItemStack(Blocks.GLASS_PANE, 1));

		ConfigurationHandler.init();
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> e) {
		e.getRegistry().registerAll(new BlockChest(), new BlockChestBuilder(), new BlockBarrel(), new BlockFluidBarrel());

		GameRegistry.registerTileEntity(TileEntityChest.class, new ResourceLocation(MODID, "chest"));
		GameRegistry.registerTileEntity(TileEntityChestBuilder.class, new ResourceLocation(MODID, "chest_builder"));
		GameRegistry.registerTileEntity(TileEntityBarrel.class, new ResourceLocation(MODID, "barrel"));
		GameRegistry.registerTileEntity(TileEntityBarrelFluid.class, new ResourceLocation(MODID, "fluid_barrel"));
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> e) {
		Item i = new ItemBlockChest(CompactRegistry.CHEST);
		i.setRegistryName(CompactRegistry.CHEST.getRegistryName());
		e.getRegistry().register(i);

		i = new ItemBlock(CompactRegistry.CHEST_BUILDER);
		i.setRegistryName(CompactRegistry.CHEST_BUILDER.getRegistryName());
		i.setCreativeTab(TAB);
		e.getRegistry().register(i);

		i = new ItemBackpack();
		i.setRegistryName("backpack");
		e.getRegistry().register(i);

		i = new ItemBlock(CompactRegistry.BARREL);
		i.setRegistryName(CompactRegistry.BARREL.getRegistryName());
		e.getRegistry().register(i);

		i = new ItemBlock(CompactRegistry.FLUID_BARREL);
		i.setRegistryName(CompactRegistry.FLUID_BARREL.getRegistryName());
		e.getRegistry().register(i);
	}

	public static int getColorFromHue(int hue) {
		Color color = (hue == -1 ? Color.white : Color.getHSBColor(hue / 360f, 0.5f, 0.5f).brighter());
		return color.getRGB();
	}

	public static int getColorFromNBT(ItemStack stack) {
		StorageInfo info = new StorageInfo(0, 0, 0, null);
		info.deserialize(stack.getOrCreateSubCompound("BlockEntityTag").getCompoundTag("info"));
		return getColorFromHue(info.getHue());
	}
}
