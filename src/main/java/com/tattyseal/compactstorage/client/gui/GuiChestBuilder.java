package com.tattyseal.compactstorage.client.gui;

import com.tattyseal.compactstorage.CompactStorage;
import com.tattyseal.compactstorage.client.gui.elements.GuiSliderHue;
import com.tattyseal.compactstorage.client.gui.responder.GuiChestBuilderResponder;
import com.tattyseal.compactstorage.client.gui.slider.ColumnFormatType;
import com.tattyseal.compactstorage.client.gui.slider.HueFormatType;
import com.tattyseal.compactstorage.client.gui.slider.RowFormatType;
import com.tattyseal.compactstorage.packet.MessageCraftChest;
import com.tattyseal.compactstorage.packet.MessageUpdateBuilder;
import com.tattyseal.compactstorage.tileentity.TileEntityChestBuilder;
import com.tattyseal.compactstorage.util.RenderUtil;
import com.tattyseal.compactstorage.util.StorageInfo;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Toby on 09/11/2014.
 */
public class GuiChestBuilder extends GuiContainer {
	public World world;
	public EntityPlayer player;
	public BlockPos pos;

	private GuiButton buttonSubmit;

	private GuiSlider hueSlider;
	private GuiSlider columnSlider;
	private GuiSlider rowSlider;

	public TileEntityChestBuilder builder;

	private static final ResourceLocation CREATIVE_INVENTORY_TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

	public GuiChestBuilder(Container container, World world, EntityPlayer player, BlockPos pos) {
		super(container);

		this.world = world;
		this.player = player;
		this.pos = pos;

		this.builder = ((TileEntityChestBuilder) world.getTileEntity(pos));

		this.xSize = 7 + 162 + 7;
		this.ySize = 7 + 108 + 13 + 54 + 4 + 18 + 7;
	}

	@Override
	public void initGui() {
		super.initGui();

		buttonSubmit = new GuiButton(4, guiLeft + 5, guiTop + 8 + 108 - 14, xSize - 31, 20, "Build");
		buttonList.add(buttonSubmit);

		int offsetY = 18;

		columnSlider = new GuiSlider(new GuiChestBuilderResponder(this), 0, guiLeft + 5, guiTop + offsetY + 22, "Columns", 1f, 24f, 9, new ColumnFormatType());
		columnSlider.setWidth((xSize / 2) - 7);
		columnSlider.setSliderValue(builder.getInfo().getSizeX(), false);
		buttonList.add(columnSlider);

		rowSlider = new GuiSlider(new GuiChestBuilderResponder(this), 1, guiLeft + ((xSize / 2)) + 3, guiTop + offsetY + 22, "Rows", 1f, 12f, 3, new RowFormatType());
		rowSlider.setWidth((xSize / 2) - 7);
		rowSlider.setSliderValue(builder.getInfo().getSizeY(), false);
		buttonList.add(rowSlider);

		hueSlider = new GuiSliderHue(new GuiChestBuilderResponder(this), 2, guiLeft + 5, guiTop + offsetY, "Hue", -1f, 360f, 180, new HueFormatType());
		hueSlider.setWidth(xSize - 10);
		hueSlider.setSliderValue(builder.getInfo().getHue(), false);
		buttonList.add(hueSlider);
	}

	@Override
	public void mouseClicked(int x, int y, int b) throws IOException {
		super.mouseClicked(x, y, b);
		hueSlider.mousePressed(mc, x, y);

		for (int t = 0; t < StorageInfo.Type.values().length; t++) {
			StorageInfo.Type type = StorageInfo.Type.values()[t];

			int startX = guiLeft + (26 * t);
			int startY = guiTop - 26;

			int endX = startX + 26;
			int endY = startY + 26;

			if (x >= startX && x <= endX) {
				if (y >= startY && y <= endY) {
					StorageInfo info = new StorageInfo(builder.getInfo().getSizeX(), builder.getInfo().getSizeY(), builder.getInfo().getHue(), type);
					CompactStorage.NETWORK.sendToServer(new MessageUpdateBuilder(pos, info));
				}
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float k) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, k);

		if (builder != null) {
			boolean hoverTooltip = false;

			for (int x = 0; x < 4; x++) {
				if (x < builder.getInfo().getMaterialCost().size() && builder.getInfo().getMaterialCost().get(x) != null) {
					ItemStack stack = builder.getInfo().getMaterialCost().get(x);

					int startX = guiLeft + ((xSize / 2) - 36) + (x * 18);
					int startY = guiTop + 62;

					int endX = startX + 18;
					int endY = startY + 18;

					if (mouseX >= startX && mouseX <= endX) {
						if (mouseY >= startY && mouseY <= endY) {
							ArrayList<String> toolList = new ArrayList<String>();
							toolList.add(stack.getDisplayName());
							toolList.add(TextFormatting.AQUA + "Amount Required: " + stack.getCount());

							drawHoveringText(toolList, mouseX, mouseY, fontRenderer);
							hoverTooltip = true;
							break;
						}
					}

					RenderHelper.disableStandardItemLighting();
				}
			}

			if (!hoverTooltip) {
				for (int t = 0; t < StorageInfo.Type.values().length; t++) {
					StorageInfo.Type type = StorageInfo.Type.values()[t];

					int startX = guiLeft + (26 * t);
					int startY = guiTop - 26;

					int endX = startX + 26;
					int endY = startY + 26;

					if (mouseX >= startX && mouseX <= endX) {
						if (mouseY >= startY && mouseY <= endY) {
							ArrayList<String> toolList = new ArrayList<String>();
							toolList.add(type.name);

							drawHoveringText(toolList, mouseX, mouseY, fontRenderer);
							hoverTooltip = true;
							break;
						}
					}
				}
			}

			if (!hoverTooltip) {
				this.renderHoveredToolTip(mouseX, mouseY);
			}
		}
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float i, int j, int k) {
		super.drawGuiContainerForegroundLayer(j, k);

		for (StorageInfo.Type type : StorageInfo.Type.values()) {
			if (!type.equals(builder.getInfo().getType())) {
				drawTab(type, type.display);
			}
		}

		RenderHelper.disableStandardItemLighting();
		GL11.glColor3f(1, 1, 1);

		drawTexturedModalRect(guiLeft, guiTop, 0, 0, 7, 7);

		RenderUtil.renderBackground(this, guiLeft, guiTop, 162, 14 + 15 + 15 + 15 + 36);

		int slotX = guiLeft + (xSize / 2) - ((9 * 18) / 2);
		int slotY = guiTop + 7 + 108 + 10;

		RenderUtil.renderSlots(slotX, slotY, 9, 3);

		slotY = slotY + (3 * 18) + 4;

		RenderUtil.renderSlots(slotX, slotY, 9, 1);

		slotY = guiTop + 50 + 12;
		slotX = guiLeft + ((xSize / 2) - 36);

		RenderUtil.renderSlots(slotX, slotY, 4, 1);

		slotY = slotY + 20;

		RenderUtil.renderSlots(slotX, slotY, 4, 1);

		RenderUtil.renderSlots(guiLeft + 5 + xSize - 30, guiTop + 8 + 108 - 13, 1, 1);

		GL11.glColor3f(1, 1, 1);

		StorageInfo info = builder.getInfo();

		if (info == null) { return; }

		slotY = guiTop + 50 + 12;
		slotX = guiLeft + ((xSize / 2) - 36);

		for (int x = 0; x < info.getMaterialCost().size(); x++) {
			ItemStack stack = info.getMaterialCost().get(x);

			if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) stack.setItemDamage(0);

			RenderHelper.enableGUIStandardItemLighting();
			itemRender.renderItemIntoGUI(stack, slotX + 1 + (x * 18), slotY + 1);

			RenderHelper.disableStandardItemLighting();
		}
		fontRenderer.drawString(I18n.format("tile.chestBuilder.name"), guiLeft + 7, guiTop + 7, 0x404040);
		drawTab(builder.getInfo().getType(), builder.getInfo().getType().display);
	}

	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		StorageInfo info = builder.getInfo();

		switch (button.id) {
		case 4: {
			CompactStorage.NETWORK.sendToServer(new MessageCraftChest(pos, info));

			break;
		}
		default: {
			break;
		}
		}
	}

	/**
	 * Draws the given tab and its background, deciding whether to highlight the tab or not based off of the selected
	 * index.
	 */
	private void drawTab(StorageInfo.Type type, ItemStack stack) {
		boolean flag = type.ordinal() == builder.getInfo().getType().ordinal();
		int i = type.ordinal();
		int j = i * 28;
		int k = 0;
		int l = this.guiLeft + 26 * i;
		int i1 = this.guiTop;

		if (flag) {
			k += 32;
		}

		if (i == 5) {
			l = this.guiLeft + this.xSize - 28;
		} else if (i > 0) {
			l += i;
		}

		i1 -= 28;

		GlStateManager.disableLighting();
		GlStateManager.color(1F, 1F, 1F); //Forge: Reset color in case Items change it.
		GlStateManager.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.

		this.mc.getTextureManager().bindTexture(CREATIVE_INVENTORY_TABS);
		this.drawTexturedModalRect(l, i1, j, k, 28, 32);
		this.zLevel = 100.0F;
		this.itemRender.zLevel = 100.0F;
		l += 6;
		i1 += 8 + 1;
		GlStateManager.enableLighting();
		GlStateManager.enableRescaleNormal();
		GlStateManager.color(1f, 0f, 0f);
		this.itemRender.renderItemAndEffectIntoGUI(stack, l, i1);
		this.itemRender.renderItemOverlays(this.fontRenderer, stack, l, i1);
		GlStateManager.disableLighting();
		this.itemRender.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}
}
