package com.tattyseal.compactstorage.client;

import com.tattyseal.compactstorage.CompactRegistry;
import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.client.render.TileEntityBarrelFluidRenderer;
import com.tattyseal.compactstorage.client.render.TileEntityBarrelRenderer;
import com.tattyseal.compactstorage.client.render.TileEntityChestRenderer;
import com.tattyseal.compactstorage.tileentity.TileEntityBarrel;
import com.tattyseal.compactstorage.tileentity.TileEntityBarrelFluid;
import com.tattyseal.compactstorage.tileentity.TileEntityChest;
import com.tattyseal.compactstorage.util.ModelUtil;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value = Side.CLIENT, modid = CompactStorage.MODID)
public class ClientHandler {

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent e) {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityChest.class, new TileEntityChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrel.class, new TileEntityBarrelRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBarrelFluid.class, new TileEntityBarrelFluidRenderer());
		ModelUtil.register(CompactRegistry.CHEST_BUILDER, 0);
		ModelUtil.register(CompactRegistry.BARREL, 0);
		ModelUtil.register(CompactRegistry.FLUID_BARREL, 0);
		ModelUtil.register(CompactRegistry.CHEST, 0);
		ModelUtil.register(CompactRegistry.BACKPACK, 0);
	}

}
