package me.modforgery.cc.client;

import cpw.mods.fml.common.registry.LanguageRegistry;
import me.modforgery.cc.tileentity.TileEntityChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class GuiDoubleChest extends GuiChest
{
    public GuiDoubleChest(Container serverGuiElement, EntityPlayer player, World world, int x, int y, int z)
    {
        super("double", 176, 206, serverGuiElement, player, world, x, y, z, false);
    }
}
