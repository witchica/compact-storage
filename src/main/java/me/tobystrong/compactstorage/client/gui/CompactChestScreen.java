package me.tobystrong.compactstorage.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.slot.SlotActionType;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CompactChestScreen extends HandledScreen<CompactChestContainer> {
    public static final Identifier CHEST_SLOTS_TEXTURE = new Identifier("compact-storage", "textures/gui/chest_slots.png");
    public static final Identifier CHEST_BACKGROUND_TEXTURE = new Identifier("compact-storage", "textures/gui/chest.png");
    
    private CompactChestContainer container;

    public CompactChestScreen(CompactChestContainer container, PlayerInventory inventory, Text title) {
        super(container, inventory, title);
        this.container = container;

        this.backgroundWidth = 14 + container.inventoryWidth * 18;
        this.backgroundHeight = 114 + container.inventoryHeight * 18 + 7;
    }
    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.title, 8.0F, 6.0F, 4210752);
        this.textRenderer.draw(matrices, this.playerInventory.getDisplayName(), 8.0F, (float)(this.backgroundHeight - 96 - 3), 4210752);

        //super.drawForeground(matrices, mouseX, mouseY);
        
        this.drawMouseoverTooltip(matrices,mouseX - x, mouseY - y);
    }

    @Override
    protected void drawBackground(MatrixStack matrixStack, float delta, int mouseX, int mouseY) {
        this.renderBackground(matrixStack);
        RenderSystem.disableLighting();

        client.getTextureManager().bindTexture(CHEST_BACKGROUND_TEXTURE);

        //drawTexture(matrixStack, x, y, width, height, u, v, uWidth, vHeight, texWidth, texHeight);
        //corners

        drawTexture(matrixStack, x, y, 0, 0, 7, 7, 15, 15);
        drawTexture(matrixStack, x + backgroundWidth - 7, y, 8, 0, 7, 7, 15, 15);

        drawTexture(matrixStack, x, y + backgroundHeight - 7, 0, 8, 8, 7, 15, 15);
        drawTexture(matrixStack, x + backgroundWidth - 7, y + backgroundHeight - 7, 8, 8, 7, 7, 15, 15);

        //middle bit
        drawTexture(matrixStack, x + 7, y + 7, backgroundWidth - 14, backgroundHeight - 14, 7, 7, 1, 1, 15, 15);

        //left side
        drawTexture(matrixStack, x, y + 7, 7, backgroundHeight - 14, 0, 7, 7, 1, 15, 15);

        //right side
        drawTexture(matrixStack, x + backgroundWidth - 7, y + 7, 7, backgroundHeight - 14, 8, 7, 7, 1, 15, 15);

        //top
        drawTexture(matrixStack, x + 7, y, backgroundWidth - 14, 7, 7, 0, 1, 7, 15, 15);

        //bottom
        drawTexture(matrixStack, x + 7, y + backgroundHeight - 7, backgroundWidth - 14, 7, 7, 8, 1, 7, 15, 15);

        client.getTextureManager().bindTexture(CHEST_SLOTS_TEXTURE);
        //chest slots
        drawTexture(matrixStack, this.x + 7, this.y + 17, 0, 0, 18 * container.inventoryWidth, 18 * container.inventoryHeight, 432, 216);
        //inv slots  
        drawTexture(matrixStack, this.x + (backgroundWidth / 2) - 9 * 9, this.y + (container.inventoryHeight * 18) + 18 + 17, 0, 0, 18 * 9, 18 * 3, 432, 216);
        //hotbar slots
        drawTexture(matrixStack, this.x + (backgroundWidth / 2) - 9 * 9, this.y + (container.inventoryHeight * 18) + 18 + 60 + 17, 0, 0, 18 * 9, 18 * 1, 432, 216);
    }

    @Override
    protected boolean handleHotbarKeyPressed(int keyCode, int scanCode) {
        if (this.client.player.inventory.getCursorStack().isEmpty() && this.focusedSlot != null) {
            for(int i = 0; i < 9; ++i) {
                if(i == this.client.player.inventory.selectedSlot && container.blockEntity == null) {
                    continue;
                }

                if (this.client.options.keysHotbar[i].matchesKey(keyCode, scanCode)) {
                    this.onMouseClick(this.focusedSlot, this.focusedSlot.id, i, SlotActionType.SWAP);
                    return true;
                }
            }
         }
   
         return false;
    }
}
