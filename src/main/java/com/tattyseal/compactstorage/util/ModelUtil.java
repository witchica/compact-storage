package com.tattyseal.compactstorage.util;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

/***
 * Created by tattyseal for 3.0, moved to 2.1-1.8.9 on the 22/03/201
 */
public class ModelUtil {

	public static void register(Item item, int meta) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	public static void register(Block block, int meta) {
		register(Item.getItemFromBlock(block), meta);
	}
}
