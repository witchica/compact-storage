package com.tabithastrong.compactstorage.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.tabithastrong.compactstorage.screen.CompactChestScreenHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;

public class CompactChestScreen extends AbstractContainerScreen<CompactChestScreenHandler> {
    public static final ResourceLocation CHEST_SLOTS_TEXTURE = new ResourceLocation("compact_storage", "textures/gui/chest_slots.png");
    public static final ResourceLocation CHEST_BACKGROUND_TEXTURE = new ResourceLocation("compact_storage", "textures/gui/chest.png");
    
    private CompactChestScreenHandler container;
    private Inventory playerInventory;

    public CompactChestScreen(CompactChestScreenHandler container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.container = container;
        this.playerInventory = inventory;

        this.imageWidth = 14 + container.inventoryWidth * 18;
        this.imageHeight = 114 + container.inventoryHeight * 18 + 7;
    }
    @Override
    protected void renderLabels(PoseStack matrices, int mouseX, int mouseY) {
        this.font.draw(matrices, this.title, 8.0F, 6.0F, 4210752);
        this.font.draw(matrices, this.playerInventory.getDisplayName(), 8.0F, (float)(this.imageHeight - 96 - 3), 4210752);

        //super.drawForeground(matrices, mouseX, mouseY);
        
        this.renderTooltip(matrices,mouseX - leftPos, mouseY - topPos);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float delta, int mouseX, int mouseY) {
        this.renderBackground(matrixStack);
        //RenderSystem.disableLighting();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, CHEST_BACKGROUND_TEXTURE);

        //drawTexture(matrixStack, x, y, width, height, u, v, uWidth, vHeight, texWidth, texHeight);
        //corners

        blit(matrixStack, leftPos, topPos, 0, 0, 7, 7, 15, 15);
        blit(matrixStack, leftPos + imageWidth - 7, topPos, 8, 0, 7, 7, 15, 15);

        blit(matrixStack, leftPos, topPos + imageHeight - 7, 0, 8, 8, 7, 15, 15);
        blit(matrixStack, leftPos + imageWidth - 7, topPos + imageHeight - 7, 8, 8, 7, 7, 15, 15);

        //middle bit
        blit(matrixStack, leftPos + 7, topPos + 7, imageWidth - 14, imageHeight - 14, 7, 7, 1, 1, 15, 15);

        //left side
        blit(matrixStack, leftPos, topPos + 7, 7, imageHeight - 14, 0, 7, 7, 1, 15, 15);

        //right side
        blit(matrixStack, leftPos + imageWidth - 7, topPos + 7, 7, imageHeight - 14, 8, 7, 7, 1, 15, 15);

        //top
        blit(matrixStack, leftPos + 7, topPos, imageWidth - 14, 7, 7, 0, 1, 7, 15, 15);

        //bottom
        blit(matrixStack, leftPos + 7, topPos + imageHeight - 7, imageWidth - 14, 7, 7, 8, 1, 7, 15, 15);

        RenderSystem.setShaderTexture(0, CHEST_SLOTS_TEXTURE);
        //chest slots
        blit(matrixStack, this.leftPos + 7, this.topPos + 17, 0, 0, 18 * container.inventoryWidth, 18 * container.inventoryHeight, 432, 216);
        //inv slots  
        blit(matrixStack, this.leftPos + (imageWidth / 2) - 9 * 9, this.topPos + (container.inventoryHeight * 18) + 18 + 17, 0, 0, 18 * 9, 18 * 3, 432, 216);
        //hotbar slots
        blit(matrixStack, this.leftPos + (imageWidth / 2) - 9 * 9, this.topPos + (container.inventoryHeight * 18) + 18 + 60 + 17, 0, 0, 18 * 9, 18 * 1, 432, 216);
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
