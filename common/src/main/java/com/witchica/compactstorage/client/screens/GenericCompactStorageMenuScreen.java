package com.witchica.compactstorage.client.screens;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Vector2i;
import com.witchica.compactstorage.data.StorageType;

public class GenericCompactStorageMenuScreen extends AbstractContainerScreen<GenericCompactStorageMenu> {
    public static final Identifier STANDARD_SLOT = Identifier.fromNamespaceAndPath("compact_storage", "textures/gui/slots/normal.png");
    public static final Identifier BACKGROUND_LOCATION = Identifier.fromNamespaceAndPath("compact_storage", "textures/gui/inventory_background.png");

    private static final Vector2i DEFAULT_SLOTS = new Vector2i(0,0);

    private final int chestInvSizeX;
    private final int chestInvSizeY;
    private final int playerInvSizeX;
    private final int playerInvSizeY;
    private final int playerInvOffsetX;

    private final StorageType storageType;
    private final Vector2i invCoords;
    private final boolean fancyRendering;

    private int inventorySizeX = 9;
    private int inventorySizeY = 3;

    public GenericCompactStorageMenuScreen(GenericCompactStorageMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 7 + 7 + (menu.inventoryWidth * 18), 17 + (menu.inventoryHeight * 18) + 7 + 17+(3 * 18) + 4 + 18 + 7);

        this.inventorySizeX = menu.inventoryWidth;
        this.inventorySizeY = menu.inventoryHeight;

        this.storageType = menu.getStorageType();
        this.chestInvSizeX = 7 + 7 + (menu.inventoryWidth * 18);
        this.chestInvSizeY = 17 + (inventorySizeY * 18) + 7;

        this.playerInvSizeX = 7 + 7 + (9 * 18);
        this.playerInvSizeY = 17+(3 * 18) + 4 + 18 + 7;

        this.playerInvOffsetX = (chestInvSizeX / 2) - (playerInvSizeX / 2);


        this.inventoryLabelX = 7 + playerInvOffsetX;
        this.inventoryLabelY = 17 + (18 * inventorySizeY) + 11 + 7;

        this.fancyRendering = CompactStorage.config().useFancyInventoryRendering;
        this.invCoords = fancyRendering ? storageType.getInventoryCoords() : DEFAULT_SLOTS;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        blit9slice(graphics, BACKGROUND_LOCATION, leftPos, topPos, chestInvSizeX, chestInvSizeY, invCoords.x * 15, invCoords.y * 15, 7, 128, 128);
        blit9slice(graphics, BACKGROUND_LOCATION, leftPos + playerInvOffsetX, topPos + chestInvSizeY + 4, playerInvSizeX, playerInvSizeY, 0, 0, 7, 128, 128);

        if(fancyRendering) {
            renderSlots(graphics, storageType, leftPos + 7, topPos + 17, inventorySizeX, inventorySizeY);
        } else {
            renderSlots(graphics, leftPos + 7, topPos + 17, inventorySizeX, inventorySizeY);
        }

        renderSlots(graphics, leftPos + 7 + playerInvOffsetX, topPos + chestInvSizeY + 4 + 17, 9, 3);
        renderSlots(graphics, leftPos + 7 + playerInvOffsetX, topPos + chestInvSizeY + 4 + 17 + (3 * 18) + 4, 9, 1);
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }

    public void renderSlots(GuiGraphicsExtractor guiGraphics, StorageType type, int x, int y, int slotsX, int slotsY) {
        renderSlots(guiGraphics, type.getSlotsTexture(), x, y, slotsX, slotsY);
    }

    public void renderSlots(GuiGraphicsExtractor guiGraphics, int x, int y, int slotsX, int slotsY) {
        renderSlots(guiGraphics, STANDARD_SLOT, x, y, slotsX, slotsY);
    }

    private void renderSlots(GuiGraphicsExtractor guiGraphics, Identifier texture, int x, int y, int slotsX, int slotsY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 0, 0, slotsX * 18, slotsY * 18, 18, 18);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
        graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, fancyRendering ? 0xff000000 + storageType.getUiTitleColor() : -12566464, false);
        graphics.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, -12566464, false);
    }

    public void blit9slice(GuiGraphicsExtractor guiGraphics, Identifier texture, int x, int y, int width, int height, int textureOffsetX, int textureOffsetY, int segmentSize, int textureSizeX, int textureSizeY) {
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
