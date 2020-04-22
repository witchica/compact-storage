package me.tobystrong.compactstorage.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CompactChestScreen extends ContainerScreen<CompactChestContainer> {
    public static final ResourceLocation CHEST_SLOTS_TEXTURE = new ResourceLocation("compact-storage", "textures/gui/chest_slots.png");
    public static final ResourceLocation CHEST_BACKGROUND_TEXTURE = new ResourceLocation("compact-storage", "textures/gui/chest.png");
    
    private CompactChestContainer container;

    public CompactChestScreen(CompactChestContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.container = container;

        this.xSize = 14 + container.inventoryWidth * 18;
        this.ySize = 114 + container.inventoryHeight * 18 + 7;
    }
    
    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 - 3), 4210752);

        this.renderHoveredToolTip(mouseX - guiLeft, mouseY - guiTop);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float delta, int mouseX, int mouseY) {
        this.renderBackground();
        RenderSystem.disableLighting();

        minecraft.getTextureManager().bindTexture(CHEST_BACKGROUND_TEXTURE);

        //blit(x, y, width, height, u, v, uWidth, vHeight, texWidth, texHeight);
        //corners


        blit(guiLeft, guiTop, 0, 0, 7, 7, 15, 15);
        blit(guiLeft + xSize - 7, guiTop, 8, 0, 7, 7, 15, 15);

        blit(guiLeft, guiTop + ySize - 7, 0, 8, 8, 7, 15, 15);
        blit(guiLeft + xSize - 7, guiTop + ySize - 7, 8, 8, 7, 7, 15, 15);

        //middle bit
        blit(guiLeft + 7, guiTop + 7, xSize - 14, ySize - 14, 7, 7, 1, 1, 15, 15);

        //left side
        blit(guiLeft, guiTop + 7, 7, ySize - 14, 0, 7, 7, 1, 15, 15);

        //right side
        blit(guiLeft + xSize - 7, guiTop + 7, 7, ySize - 14, 8, 7, 7, 1, 15, 15);

        //top
        blit(guiLeft + 7, guiTop, xSize - 14, 7, 7, 0, 1, 7, 15, 15);

        //bottom
        blit(guiLeft + 7, guiTop + ySize - 7, xSize - 14, 7, 7, 8, 1, 7, 15, 15);

        minecraft.getTextureManager().bindTexture(CHEST_SLOTS_TEXTURE);
        //chest slots
        blit(this.guiLeft + 7, this.guiTop + 17, 0, 0, 18 * container.inventoryWidth, 18 * container.inventoryHeight, 432, 216);
        //inv slots  
        blit(this.guiLeft + (xSize / 2) - 9 * 9, this.guiTop + (container.inventoryHeight * 18) + 18 + 17, 0, 0, 18 * 9, 18 * 3, 432, 216);
        //hotbar slots
        blit(this.guiLeft + (xSize / 2) - 9 * 9, this.guiTop + (container.inventoryHeight * 18) + 18 + 60 + 17, 0, 0, 18 * 9, 18 * 1, 432, 216);
    }



    @Override
    protected boolean func_195363_d(int keyCode, int scanCode) {
        if (this.minecraft.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null) {
            for(int i = 0; i < 9; ++i) {
                if(i == this.minecraft.player.inventory.currentItem) {
                    continue;
                }

                if (this.minecraft.gameSettings.keyBindsHotbar[i].isActiveAndMatches(InputMappings.getInputByCode(keyCode, scanCode))) {
                    this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, i, ClickType.SWAP);
                    return true;
                }
            }
        }

        return false;
    }
}
