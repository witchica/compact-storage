package com.witchica.compactstorage.common.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.witchica.compactstorage.CompactStoragePlatform;
import com.witchica.compactstorage.common.screen.CompactChestScreenHandler;
import com.witchica.compactstorage.common.util.CompactStorageUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import org.joml.Vector2i;

import java.util.HashMap;
import java.util.Map;

public class CompactChestScreen extends AbstractContainerScreen<CompactChestScreenHandler> {
    public static final ResourceLocation CHEST_SLOTS_TEXTURE = new ResourceLocation("compact_storage", "textures/gui/inventory_slots.png");
    public static final ResourceLocation CHEST_BACKGROUND_TEXTURE = new ResourceLocation("compact_storage", "textures/gui/inventory_background.png");
    
    private CompactChestScreenHandler container;
    private Inventory playerInventory;

    private final Vector2i slotUvOffset;
    private final Vector2i backgroundUvOffset;
    private static final Vector2i ZERO = new Vector2i(0,0);

    public CompactChestScreen(CompactChestScreenHandler container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.container = container;
        this.playerInventory = inventory;

        this.imageWidth = 14 + container.inventoryWidth * 18;
        this.imageHeight = (container.inventoryHeight * 18) + 55 + (18 * 4);

        this.slotUvOffset = new Vector2i(container.visualType.getSlotOffset()).mul(18);
        this.backgroundUvOffset =  new Vector2i(container.visualType.getBackgroundOffset()).mul(15);
    }
    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        int playerInventoryOffset = (imageWidth / 2) - 9 * 9 - 7;
        context.drawString(this.font, this.title, 8, 6, container.visualType == CompactStorageUtil.StorageVisualTypes.WHITE ? 4210752 : 0xDCDCDC, false);
        context.drawString(font, this.playerInventory.getDisplayName(), 8 + playerInventoryOffset, 32+ (container.inventoryHeight*18), 4210752, false);
        
        this.renderTooltip(context,mouseX - leftPos, mouseY - topPos);
    }

    @Override
    protected void renderBg(GuiGraphics matrixStack, float delta, int mouseX, int mouseY) {
        //RenderSystem.disableLighting();
        this.renderBackground(matrixStack);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, CHEST_BACKGROUND_TEXTURE);

        int playerInventoryOffset = (imageWidth / 2) - 9 * 9 - 7;

        // background
        drawInventorySection(matrixStack, leftPos, topPos, imageWidth, container.inventoryHeight * 18 + 24, backgroundUvOffset);

        drawInventorySection(matrixStack, leftPos + playerInventoryOffset, topPos + container.inventoryHeight * 18 + 24+3, 14 + (9 * 18), 7 + (18 * 4) + 7 + 7+7, ZERO);

        //chest slots
        drawInventorySlots(matrixStack, this.leftPos + 7, this.topPos + 18, container.inventoryWidth, container.inventoryHeight, slotUvOffset);
        //inv slots
        drawInventorySlots(matrixStack, this.leftPos + (imageWidth / 2) - 9 * 9, this.topPos + (container.inventoryHeight * 18) + 18 + 17+7, 9, 3, ZERO);
        //hotbar slots
        drawInventorySlots(matrixStack, this.leftPos + (imageWidth / 2) - 9 * 9, this.topPos + (container.inventoryHeight * 18) + 18 + 60 + 17+7, 9, 1, ZERO);
    }

    public void drawInventorySection(GuiGraphics matrixStack, int x, int y, int width, int height, Vector2i uvOffset) {

        matrixStack.blit(CHEST_BACKGROUND_TEXTURE, x, y, uvOffset.x, uvOffset.y, 7, 7, 128, 128);
        matrixStack.blit(CHEST_BACKGROUND_TEXTURE, x + width - 7, y, uvOffset.x + 8, uvOffset.y, 7, 7, 128, 128);

        matrixStack.blit(CHEST_BACKGROUND_TEXTURE, x, y + height - 7, uvOffset.x, uvOffset.y + 8, 8, 7, 128, 128);
        matrixStack.blit(CHEST_BACKGROUND_TEXTURE, x + width - 7, y + height - 7, uvOffset.x + 8, uvOffset.y + 8, 7, 7, 128, 128);

        //middle bit
        matrixStack.blit(CHEST_BACKGROUND_TEXTURE, x + 7, y + 7, width - 14, height - 14, uvOffset.x + 7, uvOffset.y + 7, 1, 1, 128,128);

        //left side
        matrixStack.blit(CHEST_BACKGROUND_TEXTURE, x, y + 7, 7, height - 14, uvOffset.x, uvOffset.y + 7, 7, 1, 128, 128);

        //right side
        matrixStack.blit(CHEST_BACKGROUND_TEXTURE, x + width - 7, y + 7, 7, height - 14, uvOffset.x + 8, uvOffset.y + 7, 7, 1, 128, 128);

        //top
        matrixStack.blit(CHEST_BACKGROUND_TEXTURE, x + 7, y, width - 14, 7, uvOffset.x + 7, uvOffset.y + 0, 1, 7, 128, 128);

        //bottom
        matrixStack.blit(CHEST_BACKGROUND_TEXTURE, x + 7, y + height - 7, width - 14, 7, uvOffset.x+7, uvOffset.y + 8, 1, 7, 128, 128);

    }

    public void drawInventorySlots(GuiGraphics matrixStack, int x, int y, int numSlotsX, int numSlotsY, Vector2i uvOffset) {
        for (int i = 0; i < numSlotsX; i++) {
            for (int j = 0; j < numSlotsY; j++) {
                matrixStack.blit(CHEST_SLOTS_TEXTURE, x + (18 * i), y + (18 * j), 18, 18, uvOffset.x,uvOffset.y, 18, 18, 128, 128);
            }
        }
    }

    @Override
    protected boolean checkHotbarKeyPressed(int keyCode, int scanCode) {
        if (this.minecraft.player.containerMenu.getCarried().isEmpty() && this.hoveredSlot != null) {
            for(int i = 0; i < 9; ++i) {
                if(i == this.minecraft.player.getInventory().selected && container.blockEntity == null) {
                    continue;
                }

                if (this.minecraft.options.keyHotbarSlots[i].matches(keyCode, scanCode)) {
                    this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, i, ClickType.SWAP);
                    return true;
                }
            }
         }
   
         return false;
    }
}
