package com.tattyseal.compactstorage;

import com.tattyseal.compactstorage.block.BlockBarrel;
import com.tattyseal.compactstorage.block.BlockChest;
import com.tattyseal.compactstorage.block.BlockChestBuilder;
import com.tattyseal.compactstorage.block.BlockFluidBarrel;
import com.tattyseal.compactstorage.client.render.TileEntityBarrelFluidRenderer;
import com.tattyseal.compactstorage.client.render.TileEntityBarrelRenderer;
import com.tattyseal.compactstorage.client.render.TileEntityChestRenderer;
import com.tattyseal.compactstorage.creativetabs.CreativeTabCompactStorage;
import com.tattyseal.compactstorage.event.CompactStorageEventHandler;
import com.tattyseal.compactstorage.event.ConnectionHandler;
import com.tattyseal.compactstorage.item.ItemBackpack;
import com.tattyseal.compactstorage.item.ItemBlockChest;
import com.tattyseal.compactstorage.network.handler.C01HandlerUpdateBuilder;
import com.tattyseal.compactstorage.network.handler.C02HandlerCraftChest;
import com.tattyseal.compactstorage.network.packet.C01PacketUpdateBuilder;
import com.tattyseal.compactstorage.network.packet.C02PacketCraftChest;
import com.tattyseal.compactstorage.proxy.IProxy;
import com.tattyseal.compactstorage.tileentity.TileEntityBarrel;
import com.tattyseal.compactstorage.tileentity.TileEntityBarrelFluid;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;
import com.tattyseal.compactstorage.tileentity.TileEntityChestBuilder;
import com.tattyseal.compactstorage.util.LogHelper;
import com.tattyseal.compactstorage.util.ModelUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.awt.*;

/**
 * Created by Toby on 06/11/2014.
 * Updated for 3.0 on the 16/02/2018
 */
@Mod(modid = CompactStorage.ID, name = "CompactStorage", version = "3.1", guiFactory = "com.tattyseal.compactstorage.client.gui.factory.CompactStorageGuiFactory")
@Mod.EventBusSubscriber
public class CompactStorage
{
    @Mod.Instance(CompactStorage.ID)
    public static CompactStorage instance;

    @SidedProxy(clientSide = "com.tattyseal.compactstorage.proxy.ClientProxy", serverSide = "com.tattyseal.compactstorage.proxy.ServerProxy", modId = CompactStorage.ID)
    public static IProxy proxy;

    public static final CreativeTabs tabCS = new CreativeTabCompactStorage();
    public static final Logger logger = LogManager.getLogger("CompactStorage");
    public SimpleNetworkWrapper wrapper;
    
    public static final String ID = "compactstorage";

    @GameRegistry.ObjectHolder(ID)
    public static class ModBlocks
    {
        public static Block chest;
        public static Block chestBuilder;
        public static Block barrel;
        public static Block barrel_fluid;
    }

    @GameRegistry.ObjectHolder(ID)
    public static class ModItems
    {
        public static ItemBlock itemBlockBarrel;
        public static ItemBlock itemBlockBarrel_fluid;
        public static ItemBlockChest ibChest;
        public static Item backpack;
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> e)
    {
        e.getRegistry().registerAll(
                ModBlocks.chest = new BlockChest(),
                ModBlocks.chestBuilder = new BlockChestBuilder(),
                ModBlocks.barrel = new BlockBarrel(),
                ModBlocks.barrel_fluid = new BlockFluidBarrel()
        );

        GameRegistry.registerTileEntity(TileEntityChest.class, "tileChest");
        GameRegistry.registerTileEntity(TileEntityChestBuilder.class, "tileChestBuilder");
        GameRegistry.registerTileEntity(TileEntityBarrel.class, "tileBarrel");
        GameRegistry.registerTileEntity(TileEntityBarrelFluid.class, "tileBarrel_fluid");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e)
    {
        ModItems.ibChest = new ItemBlockChest(ModBlocks.chest);
        ModItems.ibChest.setRegistryName("compactChest");
        e.getRegistry().register(ModItems.ibChest);

        ItemBlock ibChestBuilder = new ItemBlock(ModBlocks.chestBuilder);
        ibChestBuilder.setRegistryName("chestBuilder");
        ibChestBuilder.setCreativeTab(tabCS);
        e.getRegistry().register(ibChestBuilder);

        ModItems.backpack = new ItemBackpack();
        ModItems.backpack.setRegistryName("backpack");
        e.getRegistry().register(ModItems.backpack);

        ModItems.itemBlockBarrel = new ItemBlock(ModBlocks.barrel);
        ModItems.itemBlockBarrel.setRegistryName(ModBlocks.barrel.getRegistryName());
        e.getRegistry().register(ModItems.itemBlockBarrel);

        ModItems.itemBlockBarrel_fluid = new ItemBlock(ModBlocks.barrel_fluid);
        ModItems.itemBlockBarrel_fluid.setRegistryName(ModBlocks.barrel_fluid.getRegistryName());
        e.getRegistry().register(ModItems.itemBlockBarrel_fluid);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent e)
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChest.class, new TileEntityChestRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrel.class, new TileEntityBarrelRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrelFluid.class, new TileEntityBarrelFluidRenderer());

        ModelUtil.registerChest();
        ModelUtil.registerBlock(ModBlocks.chestBuilder, 0, "compactstorage:chestBuilder");

        ModelUtil.registerItem(ModItems.itemBlockBarrel, 0, "compactstorage:barrel");
        ModelUtil.registerItem(ModItems.itemBlockBarrel_fluid, 0, "compactstorage:barrel_fluid");

        ModelUtil.registerBlock(ModBlocks.chest, 0, "compactstorage:compactchest");
        ModelUtil.registerItem(ModItems.backpack, 0, "compactstorage:backpack");

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        OreDictionary.registerOre("barsIron", Blocks.IRON_BARS);
        OreDictionary.registerOre("blockChest", Blocks.CHEST);
        OreDictionary.registerOre("itemClay", Items.CLAY_BALL);

        OreDictionary.registerOre("string", Items.STRING);
        OreDictionary.registerOre("wool", Blocks.WOOL);
        
        wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(CompactStorage.ID);
        wrapper.registerMessage(C01HandlerUpdateBuilder.class, C01PacketUpdateBuilder.class, 0, Side.SERVER);
        wrapper.registerMessage(C02HandlerCraftChest.class, C02PacketCraftChest.class, 1, Side.SERVER);

        ConfigurationHandler.configFile = event.getSuggestedConfigurationFile();

        MinecraftForge.EVENT_BUS.register(new ConnectionHandler());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerRenderers();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new CompactStorageEventHandler());

        GameRegistry.addShapedRecipe(new ResourceLocation("compactstorage", "chest_builder"), null, new ItemStack(ModBlocks.chestBuilder, 1), "ILI", "ICI", "ILI", 'I', new ItemStack(Items.IRON_INGOT, 1), 'C', new ItemStack(Blocks.CHEST, 1), 'L', new ItemStack(Blocks.LEVER, 1));
        GameRegistry.addShapedRecipe(new ResourceLocation("compactstorage", "barrel"), null, new ItemStack(ModBlocks.barrel, 1), "III", "GCG", "III", 'I', new ItemStack(Items.IRON_INGOT, 1), 'G', new ItemStack(Blocks.IRON_BLOCK, 1), 'C', new ItemStack(Blocks.CHEST, 1));
        GameRegistry.addShapedRecipe(new ResourceLocation("compactstorage", "drum"), null, new ItemStack(ModBlocks.barrel_fluid, 1), "ICI", "GIG", "ICI", 'I', new ItemStack(Items.IRON_INGOT, 1), 'G', new ItemStack(Blocks.IRON_BLOCK, 1), 'C', new ItemStack(Blocks.GLASS_PANE, 1));

        ConfigurationHandler.init();
    }

    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<TileEntity> e) {
        TileEntity te = e.getObject();
        if (te instanceof TileEntityChest) {
            e.addCapability(new ResourceLocation(ID, "invwrapper"), new InvWrappingCap((IInventory) te));
        }
    }

    public static int getColorFromHue(int hue)
    {
        Color color = (hue == -1 ? Color.white : Color.getHSBColor(hue / 360f, 0.5f, 0.5f).brighter());
        return color.getRGB();
    }

    public static int getColorFromNBT(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();

        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("hue"))
        {
            int hue = stack.getTagCompound().getInteger("hue");
            return getColorFromHue(hue);
        }

        if(stack.hasTagCompound() && !stack.getTagCompound().hasKey("hue") && stack.getTagCompound().hasKey("color"))
        {
            String color = "";

            if(tag.getTag("color") instanceof NBTTagInt)
            {
                color = String.format("#%06X", (0xFFFFFF & tag.getInteger("color")));
            }
            else
            {
                color = tag.getString("color");

                if(color.startsWith("0x"))
                {
                    color = "#" + color.substring(2);
                }
            }

            if(!color.isEmpty())
            {
                Color c = Color.decode(color);
                float[] hsbVals = new float[3];

                hsbVals = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbVals);
                tag.setInteger("hue", (int) (hsbVals[0] * 360));
            }
        }

        return 0xFFFFFF;
    }

    private static class InvWrappingCap implements ICapabilityProvider {

        private InvWrapper wrapper;

        private InvWrappingCap(IInventory inventory) {
            wrapper = new InvWrapper(inventory);
        }

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            if (this.hasCapability(capability, facing)) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(wrapper);
            } else {
                return null;
            }
        }

    }
}
