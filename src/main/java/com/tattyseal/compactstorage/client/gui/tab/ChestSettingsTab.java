package com.tattyseal.compactstorage.client.gui.tab;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.client.gui.GuiChest;
import com.tattyseal.compactstorage.network.PacketApplyChestUpdate;
import com.tattyseal.compactstorage.util.ChestUtil;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemStack;

public class ChestSettingsTab extends ITab 
{
	public int invX;
	public int invY;
	
	public GuiButton plusX;
	public GuiButton minusX;
	public GuiButton plusZ;
	public GuiButton minusZ;
	
	public GuiButton apply;
	
	public GuiTextField xField;
	public GuiTextField zField;
	
	public GuiChest chest;
	
	public ChestSettingsTab(GuiChest container, ItemStack item, String name, int x, int y, boolean localizeName, int invX, int invY) 
	{
		super(container, item, name, x, y, localizeName);
		this.chest = container;
		this.invX = invX;
		this.invY = invY;
		
		int bpxm = chest.getGuiLeft() + 7;
		int bpxp = (chest.getGuiLeft() + 7 + (Math.max(9, invX) * 18) + 7) - 27;
		
		this.plusX = new GuiButton(0, bpxp, chest.getGuiTop() + 8, 20, 20, "+");
		this.minusX = new GuiButton(1, bpxm, chest.getGuiTop() + 8, 20, 20, "-");
		this.plusZ = new GuiButton(2, bpxp, chest.getGuiTop() + 33, 20, 20, "+");
		this.minusZ = new GuiButton(3, bpxm, chest.getGuiTop() + 33, 20, 20, "-");
		this.apply = new GuiButton(4, bpxm + 25, chest.getGuiTop() + 58, (Math.max(9, invX) * 18) - 49, 20, "Apply Changes");

		this.xField = new GuiTextField(mc.fontRenderer, bpxm + 25, chest.getGuiTop() + 8, (Math.max(9, invX) * 18) - 49, 20);
		this.zField = new GuiTextField(mc.fontRenderer, bpxm + 25, chest.getGuiTop() + 33, (Math.max(9, invX) * 18) - 49, 20);
	
		this.xField.setEnabled(false);
		this.zField.setEnabled(false);
		
		this.xField.setText(invX + "");
		this.zField.setText(invY + "");
	}

	@Override
	public void selected()
	{
		chest.getButtonList().add(plusX);
		chest.getButtonList().add(plusZ);
		chest.getButtonList().add(minusX);
		chest.getButtonList().add(minusZ);
		chest.getButtonList().add(apply);
	}

	@Override
	public void deselected() 
	{
		chest.getButtonList().clear();
 	}

	@Override
	public void drawBackground(int guiLeft, int guiTop) 
	{
		this.xField.setText(invX + "");
		this.zField.setText(invY + "");
		
        xField.drawTextBox();
        zField.drawTextBox();
	}

	@Override
	public void drawForeground(int guiLeft, int guiTop) 
	{
		
	}

	@Override
	public void buttonClicked(GuiButton button) 
	{
		switch(button.id)
		{
			case 0:
			{
				invX = ChestUtil.clamp(invX + 1, 1, 24);
				break;
			}
			case 1:
			{
				invX = ChestUtil.clamp(invX - 1, 1, 24);
				break;
			}
			case 2:
			{
				invY = ChestUtil.clamp(invY + 1, 1, 12);
				break;
			}
			case 3:
			{
				invY = ChestUtil.clamp(invY - 1, 1, 12);
				break;
			}
			case 4:
			{
				CompactStorage.instance.wrapper.sendToServer(new PacketApplyChestUpdate(chest.pos.getX(), chest.pos.getY(), chest.pos.getZ(), invX, invY));
				mc.displayGuiScreen(null);
			}
		}
	}

	@Override
	public void mouseClicked(int x, int z, int b) 
	{
		xField.mouseClicked(x, z, b);
		zField.mouseClicked(x, z, b);
	}

	@Override
	public void keyTyped(char c, int id) 
	{
		if(xField.isFocused()) xField.textboxKeyTyped(c, id);
		if(zField.isFocused()) zField.textboxKeyTyped(c, id);
	}

	@Override
	public boolean areTextboxesInFocus() 
	{
		return xField.isFocused() || zField.isFocused();
	}
}
