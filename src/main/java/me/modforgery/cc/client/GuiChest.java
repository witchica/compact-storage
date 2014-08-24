package me.modforgery.cc.client;

import cpw.mods.fml.common.registry.LanguageRegistry;
import me.modforgery.cc.tileentity.TileEntityChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by Toby on 19/08/2014.
 */
public class GuiChest extends GuiContainer
{
    public Container container;
    public EntityPlayer player;
    public World world;

    public int x;
    public int y;
    public int z;

    public ResourceLocation bg;

    public GuiChest(String name, int w, int h, Container serverGuiElement, EntityPlayer player, World world, int x, int y, int z)
    {
        super(serverGuiElement);

        this.container = serverGuiElement;
        this.player = player;
        this.world = world;

        this.x = x;
        this.y = y;
        this.z = z;

        this.xSize = w;
        this.ySize = h;

        this.bg = new ResourceLocation("compactchests", "textures/gui/container/" + name + "_chest.png");
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float i, int j, int k)
    {
        this.mc.renderEngine.bindTexture(bg);

        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
