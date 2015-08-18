package com.tattyseal.compactstorage.compat;

import com.tattyseal.compactstorage.tileentity.TileEntityChest;

import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class JabbaCompat implements ICompat
{
	public String modid()
	{
		return "JABBA";
	}
	
	@Override
	public void registerCompat() throws Exception
	{
		mcp.mobius.betterbarrels.common.items.dolly.api.MovableRegistrar.INSTANCE.registerHandler(TileEntityChest.class, new CompactStorageJabbaHandler());
		
		/***
		 * To anyone reading this. If you know of a way to accomplish what I am doing here without hacking JABBA to badly, open an issue.
		 */
		
		Class dolly = mcp.mobius.betterbarrels.common.items.dolly.ItemBarrelMover.class;
		Item dollyItem = mcp.mobius.betterbarrels.BetterBarrels.itemMover;
		
		Field classExtensionsField = dolly.getDeclaredField("classExtensions");
		Field classExtensionsNamesField = dolly.getDeclaredField("classExtensionsNames");
		Field classMapField = dolly.getDeclaredField("classMap");
		
		boolean cef = classExtensionsField.isAccessible();
		boolean cenf = classExtensionsNamesField.isAccessible();
		boolean cmf = classMapField.isAccessible();
		
		classExtensionsField.setAccessible(true);
		classExtensionsNamesField.setAccessible(true);
		classMapField.setAccessible(true);
		
		ArrayList<Class> classExtensions = (ArrayList<Class>) classExtensionsField.get(dollyItem);
		ArrayList<String> classExtensionsNames = (ArrayList<String>) classExtensionsNamesField.get(dollyItem);
		HashMap<String, Class> classMap = (HashMap<String, Class>) classMapField.get(dollyItem);
		
		classExtensionsNames.add("com.workshop.compactstorage.tileentity.TileEntityChest");
		classExtensions.add(Class.forName("com.workshop.compactstorage.tileentity.TileEntityChest"));
		classMap.put("com.workshop.compactstorage.tileentity.TileEntityChest", Class.forName("com.workshop.compactstorage.tileentity.TileEntityChest"));
	
		classExtensionsField.setAccessible(cef);
		classExtensionsNamesField.setAccessible(cenf);
		classMapField.setAccessible(cmf);
	}
	
	private class CompactStorageJabbaHandler implements mcp.mobius.betterbarrels.common.items.dolly.api.IDollyHandler
	{
		@Override
		public void onContainerPickup(World world, int x, int y, int z, TileEntity tile) 
		{
			((TileEntityChest) tile).direction = null;
		}
	}
}
