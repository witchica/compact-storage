package com.witchica.compactstorage.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.witchica.compactstorage.screen.CompactChestScreenHandler;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CompactChestScreen extends HandledScreen<CompactChestScreenHandler> {
    public static final Identifier CHEST_SLOTS_TEXTURE = new Identifier("compact_storage", "textures/gui/chest_slots.png");
    public static final Identifier CHEST_BACKGROUND_TEXTURE = new Identifier("compact_storage", "textures/gui/chest.png");
    
    private CompactChestScreenHandler container;
    private PlayerInventory playerInventory;

    public CompactChestScreen(CompactChestScreenHandler container, PlayerInventory inventory, Text title) {
        super(container, inventory, title);
        this.container = container;
        this.playerInventory = inventory;

        this.backgroundWidth = 14 + container.inventoryWidth * 18;
        this.backgroundHeight = 114 + container.inventoryHeight * 18 + 7;
    }
    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, 8, 6, 4210752, false);
        context.drawText(textRenderer, this.playerInventory.getDisplayName(), 8, this.backgroundHeight-96-3, 4210752, false);
        
        this.drawMouseoverTooltip(context,mouseX - x, mouseY - y);
    }

    @Override
    protected void drawBackground(DrawContext matrixStack, float delta, int mouseX, int mouseY) {
        //RenderSystem.disableLighting();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, CHEST_BACKGROUND_TEXTURE);

        //drawTexture(matrixStack, x, y, width, height, u, v, uWidth, vHeight, texWidth, texHeight);
        //corners

        matrixStack.drawTexture(CHEST_BACKGROUND_TEXTURE, x, y, 0, 0, 7, 7, 15, 15);
        matrixStack.drawTexture(CHEST_BACKGROUND_TEXTURE, x + backgroundWidth - 7, y, 8, 0, 7, 7, 15, 15);

        matrixStack.drawTexture(CHEST_BACKGROUND_TEXTURE, x, y + backgroundHeight - 7, 0, 8, 8, 7, 15, 15);
        matrixStack.drawTexture(CHEST_BACKGROUND_TEXTURE, x + backgroundWidth - 7, y + backgroundHeight - 7, 8, 8, 7, 7, 15, 15);

        //middle bit
        matrixStack.drawTexture(CHEST_BACKGROUND_TEXTURE, x + 7, y + 7, backgroundWidth - 14, backgroundHeight - 14, 7, 7, 1, 1, 15, 15);

        //left side
        matrixStack.drawTexture(CHEST_BACKGROUND_TEXTURE, x, y + 7, 7, backgroundHeight - 14, 0, 7, 7, 1, 15, 15);

        //right side
        matrixStack.drawTexture(CHEST_BACKGROUND_TEXTURE, x + backgroundWidth - 7, y + 7, 7, backgroundHeight - 14, 8, 7, 7, 1, 15, 15);

        //top
        matrixStack.drawTexture(CHEST_BACKGROUND_TEXTURE, x + 7, y, backgroundWidth - 14, 7, 7, 0, 1, 7, 15, 15);

        //bottom
        matrixStack.drawTexture(CHEST_BACKGROUND_TEXTURE, x + 7, y + backgroundHeight - 7, backgroundWidth - 14, 7, 7, 8, 1, 7, 15, 15);


        //chest slots
        matrixStack.drawTexture(CHEST_SLOTS_TEXTURE, this.x + 7, this.y + 17, 0, 0, 18 * container.inventoryWidth, 18 * container.inventoryHeight, 432, 216);
        //inv slots  
        matrixStack.drawTexture(CHEST_SLOTS_TEXTURE, this.x + (backgroundWidth / 2) - 9 * 9, this.y + (container.inventoryHeight * 18) + 18 + 17, 0, 0, 18 * 9, 18 * 3, 432, 216);
        //hotbar slots
        matrixStack.drawTexture(CHEST_SLOTS_TEXTURE, this.x + (backgroundWidth / 2) - 9 * 9, this.y + (container.inventoryHeight * 18) + 18 + 60 + 17, 0, 0, 18 * 9, 18 * 1, 432, 216);
    }

    @Override
    protected boolean handleHotbarKeyPressed(int keyCode, int scanCode) {
        if (this.client.player.currentScreenHandler.getCursorStack().isEmpty() && this.focusedSlot != null) {
            for(int i = 0; i < 9; ++i) {
                if(i == this.client.player.getInventory().selectedSlot && container.blockEntity == null) {
                    continue;
                }

                if (this.client.options.hotbarKeys[i].matchesKey(keyCode, scanCode)) {
                    this.onMouseClick(this.focusedSlot, this.focusedSlot.id, i, SlotActionType.SWAP);
                    return true;
                }
            }
         }
   
         return false;
    }
}
