package com.witchica.compactstorage.client.screens;

import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import org.joml.Vector2i;
import util.StorageTypes;

public class GenericCompactStorageMenuScreen extends AbstractContainerScreen<GenericCompactStorageMenu> {
    public static final Identifier STANDARD_SLOT = Identifier.fromNamespaceAndPath("compact_storage", "textures/gui/slots/normal.png");
    public static final Identifier BACKGROUND_LOCATION = Identifier.fromNamespaceAndPath("compact_storage", "textures/gui/inventory_background.png");
    private final int chestInvSizeX;
    private final int chestInvSizeY;
    private final int playerInvSizeX;
    private final int playerInvSizeY;
    private final int playerInvOffsetX;

    private final StorageTypes storageType;

    private int inventorySizeX = 9;
    private int inventorySizeY = 3;

    public GenericCompactStorageMenuScreen(GenericCompactStorageMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.storageType = menu.getStorageType();
        this.chestInvSizeX = 7 + 7 + (inventorySizeX * 18);
        this.chestInvSizeY = 17 + (inventorySizeY * 18) + 7;

        this.playerInvSizeX = 7 + 7 + (9 * 18);
        this.playerInvSizeY = 17+(3 * 18) + 4 + 18 + 7;

        this.playerInvOffsetX = (chestInvSizeX / 2) - (playerInvSizeX / 2);

        this.imageWidth = chestInvSizeX;
        this.imageHeight = chestInvSizeY + 4 + playerInvSizeY;

        this.inventoryLabelX = 7 + playerInvOffsetX;
        this.inventoryLabelY = 17 + (18 * inventorySizeY) + 11 + 7;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        Vector2i invCoords = storageType.getInventoryCoords();
        blit9slice(guiGraphics, BACKGROUND_LOCATION, leftPos, topPos, chestInvSizeX, chestInvSizeY, invCoords.x * 15, invCoords.y * 15, 7, 128, 128);
        blit9slice(guiGraphics, BACKGROUND_LOCATION, leftPos + playerInvOffsetX, topPos + chestInvSizeY + 4, playerInvSizeX, playerInvSizeY, 0, 0, 7, 128, 128);

        renderSlots(guiGraphics, storageType, leftPos + 7, topPos + 17, inventorySizeX, inventorySizeY);
        renderSlots(guiGraphics, leftPos + 7 + playerInvOffsetX, topPos + chestInvSizeY + 4 + 17, 9, 3);
        renderSlots(guiGraphics, leftPos + 7 + playerInvOffsetX, topPos + chestInvSizeY + 4 + 17 + (3 * 18) + 4, 9, 1);
    }

    public void renderSlots(GuiGraphics guiGraphics, StorageTypes type, int x, int y, int slotsX, int slotsY) {
        renderSlots(guiGraphics, type.getSlotsTexture(), x, y, slotsX, slotsY);
    }

    public void renderSlots(GuiGraphics guiGraphics, int x, int y, int slotsX, int slotsY) {
        renderSlots(guiGraphics, STANDARD_SLOT, x, y, slotsX, slotsY);
    }

    private void renderSlots(GuiGraphics guiGraphics, Identifier texture, int x, int y, int slotsX, int slotsY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 0, 0, slotsX * 18, slotsY * 18, 18, 18);
    }

    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderContents(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    public void blit9slice(GuiGraphics guiGraphics, Identifier texture, int x, int y, int width, int height, int textureOffsetX, int textureOffsetY, int segmentSize, int textureSizeX, int textureSizeY) {
        // Top Left
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                x, y,
                textureOffsetX, textureOffsetY,
                segmentSize, segmentSize,
                segmentSize, segmentSize,
                textureSizeX, textureSizeY);
        // Top Middle
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                x+segmentSize, y,
                textureOffsetX+segmentSize, textureOffsetY,
                width-(segmentSize*2), segmentSize,
                1, segmentSize,
                textureSizeX, textureSizeY);
        // Top Right
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                x+width-segmentSize, y,
                textureOffsetX+segmentSize+1, textureOffsetY,
                segmentSize, segmentSize,
                segmentSize, segmentSize,
                textureSizeX, textureSizeY);
        // Bottom left
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                x, y+height-segmentSize,
                textureOffsetX, textureOffsetY+segmentSize+1,
                segmentSize, segmentSize,
                segmentSize, segmentSize,
                textureSizeX, textureSizeY);
        // Bottom Middle
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                x+segmentSize, y+height-segmentSize,
                textureOffsetX+segmentSize, textureOffsetY+segmentSize+1,
                width-(segmentSize*2), segmentSize,
                1, segmentSize,
                textureSizeX, textureSizeY);
        // Bottom Right
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                x+width-segmentSize, y+height-segmentSize,
                textureOffsetX+segmentSize+1, textureOffsetY+segmentSize+1,
                segmentSize, segmentSize,
                segmentSize, segmentSize,
                textureSizeX, textureSizeY);
        // Left Middle
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                x, y+segmentSize,
                textureOffsetX, textureOffsetY+segmentSize,
                segmentSize, height-(segmentSize*2),
                segmentSize, 1,
                textureSizeX, textureSizeY);
        // Right Middle
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                x+width-segmentSize, y+segmentSize,
                textureOffsetX+segmentSize+1, textureOffsetY+segmentSize,
                segmentSize, height-(segmentSize*2),
                segmentSize, 1,
                textureSizeX, textureSizeY);
        // Middle
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                x+segmentSize, y+segmentSize,
                textureOffsetX+segmentSize, textureOffsetY+segmentSize,
                width-(segmentSize*2), height-(segmentSize*2),
                1, 1,
                textureSizeX, textureSizeY);
    }
}
