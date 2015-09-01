package com.tattyseal.compactstorage.client.gui.tab;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.client.gui.GuiChest;
import com.tattyseal.compactstorage.client.gui.slot.SlotChangePosition;
import com.tattyseal.compactstorage.client.gui.slot.SlotMaterial;
import com.tattyseal.compactstorage.inventory.InventoryBackpack;
import com.tattyseal.compactstorage.network.PacketApplyChestUpdate;
import com.tattyseal.compactstorage.util.ChestUtil;
import com.tattyseal.compactstorage.util.ChestUtil.Type;

import com.tattyseal.compactstorage.util.RenderUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

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
		this.apply = new GuiButton(4, bpxm + 25 + ((18 * 3) / 2), chest.getGuiTop() + 58, (Math.max(9, invX) * 18) - 49, 20, "Apply Changes");

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

		for(Object obj : container.inventorySlots.inventorySlots)
		{
			SlotChangePosition slot = (SlotChangePosition) obj;

			if(slot instanceof SlotMaterial && slot.xDisplayPosition == Integer.MIN_VALUE)
			{
				slot.changeToDefaultPosition();
			}

			if(slot.inventory instanceof InventoryPlayer)
			{
				slot.changeToDefaultPosition();
				slot.subtractFromPosition(0, 17 + chest.chest.getInvY() * 18 + 13);
				slot.addToPosition(0, 80);
			}
		}
	}

	@Override
	public void deselected() 
	{
		chest.getButtonList().clear();

		for(Object obj : container.inventorySlots.inventorySlots)
		{
			SlotChangePosition slot = (SlotChangePosition) obj;
			slot.setPosition(Integer.MIN_VALUE, slot.yDisplayPosition);
		}
 	}

	@Override
	public void drawBackground(int guiLeft, int guiTop) 
	{
		RenderUtil.renderBackground(chest, guiLeft, guiTop, Math.max(9, chest.chest.getInvX()) * 18, 50);

		this.xField.setText(invX + "");
		this.zField.setText(invY + "");
		
        xField.drawTextBox();
        zField.drawTextBox();

		int xSize = 7 + (18 * chest.chest.getInvX()) + 7;

		GL11.glColor3f(1f, 1f, 1f);

		ItemStack[] requiredMaterials = chest.chest.getRequiredUpgrades(invX, invY);

		RenderUtil.renderSlots(guiLeft + 4, guiTop + 59, 3	, 1);

		for(int i = 0; i < 3; i++)
		{
			ItemStack stack = requiredMaterials[i];

			if(stack != null)
			{
				RenderItem.getInstance().renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, stack, guiLeft + 4 + (i * 18), guiTop + 59);
				mc.fontRenderer.drawString(stack.stackSize + "", guiLeft + 4 + (i * 18) + ((18 / 2) - (mc.fontRenderer.getStringWidth("X") / 2)), guiTop + 59 + ((18 / 2) - (mc.fontRenderer.FONT_HEIGHT / 2)), 0xFF0000);
			}
			else
			{
				mc.fontRenderer.drawString("X", guiLeft + 4 + (i * 18) + ((18 / 2) - (mc.fontRenderer.getStringWidth("X") / 2)), guiTop + 59 + ((18 / 2) - (mc.fontRenderer.FONT_HEIGHT / 2)), 0xFF0000);
			}
		}

		GL11.glColor3f(1f, 1f, 1f);

		RenderUtil.renderSlots(guiLeft + 7 + (((Math.max(9, chest.chest.getInvX()) * 18) / 2) - ((9 * 18) / 2)), guiTop + 80, 9, 3);
		RenderUtil.renderSlots(guiLeft + 7 + (((Math.max(9, chest.chest.getInvX()) * 18) / 2) - ((9 * 18) / 2)), guiTop + 80 + 54 + 4, 9, 1);
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
				CompactStorage.instance.wrapper.sendToServer(new PacketApplyChestUpdate(chest.pos.getX(), chest.pos.getY(), chest.pos.getZ(), invX, invY, chest.chest instanceof InventoryBackpack ? Type.BACKPACK : Type.CHEST));
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

	@Override
	public boolean shouldChestRenderBackground()
	{
		return false;
	}
}
