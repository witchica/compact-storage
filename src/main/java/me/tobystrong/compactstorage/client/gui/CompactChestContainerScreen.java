package me.tobystrong.compactstorage.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import me.tobystrong.compactstorage.container.CompactStorageBaseContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CompactChestContainerScreen extends ContainerScreen<CompactStorageBaseContainer> {
    // textures
    public static ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("compactstorage", "textures/gui/chest.png");
    public static ResourceLocation CHEST_GUI_SLOTS_TEXTURE = new ResourceLocation("compactstorage", "textures/gui/chest_slots.png");

    public int chestWidth;
    public int chestHeight;

    public CompactChestContainerScreen(CompactStorageBaseContainer chestContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(chestContainer, inv, titleIn);

        this.chestWidth = chestContainer.chestWidth;
        this.chestHeight = chestContainer.chestHeight;

        //calculate all the sizes using the right padding
        xSize = 7 + (chestWidth * 18) + 7;
        ySize = 16 + (chestHeight * 18) + 13 + (3 * 18) + 4 + 18 + 7;

        //move the inventory tag to the center
        playerInventoryTitleY = 16 + (chestHeight * 18) + 3;
        playerInventoryTitleX = (int)(7 + ((chestWidth > 9 ? ((chestWidth - 9) * 18) / 2f : 0)));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);

        minecraft.textureManager.bindTexture(CHEST_GUI_TEXTURE);

        int playerInvX = (int)(7 + ((chestWidth > 9 ? ((chestWidth - 9) * 18) / 2f : 0)));

        //draw the center of the GUI using the texture
        blit(matrixStack, guiLeft + 7, guiTop + 7, xSize - 14, ySize - 14, 8f, 8f, 1, 1, 15, 15);

        //top left, top right
        blit(matrixStack, guiLeft, guiTop, 7, 7, 0f, 0f, 7, 7, 15, 15);
        blit(matrixStack, guiLeft + xSize - 7, guiTop, 7, 7, 8f, 0f, 7, 7, 15, 15);

        //bottom left, bottom right
        blit(matrixStack, guiLeft, guiTop + ySize - 7, 7, 7, 0f, 8f, 7, 7, 15, 15);
        blit(matrixStack, guiLeft + xSize - 7, guiTop + ySize - 7, 7, 7, 8f, 8f, 7, 7, 15, 15);

        //connects
        blit(matrixStack, guiLeft + 7, guiTop, xSize - 14, 7, 7f, 0f, 1, 7, 15, 15);
        blit(matrixStack, guiLeft + 7, guiTop + ySize - 7, xSize - 14, 7, 7f, 8f, 1, 7, 15, 15);

        //horizontal connects
        blit(matrixStack, guiLeft, guiTop + 7, 7, ySize - 14, 0f, 8f, 7, 1, 15, 15);
        blit(matrixStack, guiLeft + xSize - 7, guiTop + 7, 7, ySize - 14, 8f, 8f, 7, 1, 15, 15);

        //draw the slots in
        drawSlots(matrixStack, 7, 16, chestWidth, chestHeight);
        drawSlots(matrixStack, playerInvX, 16 + (chestHeight * 18 + 13), 9, 3);
        drawSlots(matrixStack, playerInvX, 16 + (chestHeight * 18 + 13 + (3 * 18) + 4), 9, 1);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public void drawSlots(MatrixStack matrixStack, int x, int y, int w, int h) {
        minecraft.textureManager.bindTexture(CHEST_GUI_SLOTS_TEXTURE);
        //blit the texture for slots using the amount given
        blit(matrixStack, guiLeft + x, guiTop + y, w * 18, h * 18, 0f, 0f, w * 18, h * 18, 432, 216);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
    }
}
