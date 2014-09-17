package me.modforgery.cc.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class GuiQuadrupleChest extends GuiChest
{
    public GuiQuadrupleChest(Container serverGuiElement, EntityPlayer player, World world, int x, int y, int z)
    {
        super("quadruple", 320, 256, serverGuiElement, player, world, x, y, z, true);
    }
}
