package com.witchica.compactstorage.client.screens;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.inventory.ScrollingContainerView;
import com.witchica.compactstorage.menu.GenericCompactStorageMenu;
import com.witchica.compactstorage.network.ServerboundScrollStoragePacket;
import net.blay09.mods.balm.Balm;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Vector2i;

public class GenericCompactStorageMenuScreen extends AbstractContainerScreen<GenericCompactStorageMenu> {
    public static final Identifier STANDARD_SLOT = Identifier.fromNamespaceAndPath("compact_storage", "textures/gui/slots/normal.png");
    public static final Identifier BACKGROUND_LOCATION = Identifier.fromNamespaceAndPath("compact_storage", "textures/gui/inventory_background.png");
    public static final Identifier TRASH_ICON = Identifier.fromNamespaceAndPath("compact_storage", "textures/gui/trash_icon.png");

    private static final Vector2i DEFAULT_SLOTS = new Vector2i(0,0);
    // Box border baked into the background art is 7px; the scrollbar lives inside that margin,
    // inset a couple pixels from both the outer box edge and the grid edge so it reads as an inset
    // groove rather than a rectangle glued flush to either.
    private static final int SCROLLBAR_MARGIN = 7;
    private static final int SCROLLBAR_PADDING = 2;
    private static final int SCROLLBAR_SIZE = SCROLLBAR_MARGIN - (SCROLLBAR_PADDING * 2);

    private final int chestInvSizeX;
    private final int chestInvSizeY;
    private final int playerInvSizeX;
    private final int playerInvSizeY;
    private final int playerInvOffsetX;

    private final StorageType storageType;
    private final Vector2i invCoords;
    private final boolean fancyRendering;
    private final boolean hasVoidSlot;

    private int inventorySizeX = 9;
    private int inventorySizeY = 3;

    private boolean draggingVerticalScrollbar;
    private boolean draggingHorizontalScrollbar;

    public GenericCompactStorageMenuScreen(GenericCompactStorageMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 7 + 7 + (menu.storageView.getVisibleWidth() * 18), 17 + (menu.storageView.getVisibleHeight() * 18) + 7 + 17+(3 * 18) + 4 + 18 + 7);

        this.inventorySizeX = menu.storageView.getVisibleWidth();
        this.inventorySizeY = menu.storageView.getVisibleHeight();

        this.storageType = menu.getStorageType();
        this.chestInvSizeX = 7 + 7 + (inventorySizeX * 18);
        this.chestInvSizeY = 17 + (inventorySizeY * 18) + 7;

        this.playerInvSizeX = 7 + 7 + (9 * 18);
        this.playerInvSizeY = 17+(3 * 18) + 4 + 18 + 7;

        this.playerInvOffsetX = (chestInvSizeX / 2) - (playerInvSizeX / 2);


        this.inventoryLabelX = 7 + playerInvOffsetX;
        this.inventoryLabelY = 17 + (18 * inventorySizeY) + 11 + 7;

        this.fancyRendering = CompactStorage.config().useFancyInventoryRendering;
        this.invCoords = fancyRendering ? storageType.getInventoryCoords() : DEFAULT_SLOTS;

        this.hasVoidSlot = menu.hasVoidSlot;
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

        if(hasVoidSlot) {
            extractVoidSlot(graphics, mouseX, mouseY, a);
        }

        renderSlots(graphics, leftPos + 7 + playerInvOffsetX, topPos + chestInvSizeY + 4 + 17, 9, 3);
        renderSlots(graphics, leftPos + 7 + playerInvOffsetX, topPos + chestInvSizeY + 4 + 17 + (3 * 18) + 4, 9, 1);

        extractScrollbars(graphics);

        super.extractRenderState(graphics, mouseX, mouseY, a);
    }

    // Painted as a mask over the track with a gap left at the thumb position, rather than drawing
    // the thumb itself - the box's own background art shows through the gap, so this doesn't need
    // to know the storage type's color/pattern.
    private void extractScrollbars(GuiGraphicsExtractor graphics) {
        ScrollingContainerView view = menu.storageView;

        if(view.needsVerticalScroll()) {
            int trackX = verticalScrollbarX();
            int trackY = topPos + 17;
            int trackHeight = inventorySizeY * 18;
            int maxScroll = view.getMaxScrollY();
            int thumbHeight = Math.max(12, trackHeight * view.getVisibleHeight() / view.getRealHeight());
            int thumbY = trackY + (maxScroll == 0 ? 0 : ((trackHeight - thumbHeight) * view.getScrollY() / maxScroll));

            if(thumbY > trackY) {
                graphics.fill(trackX, trackY, trackX + SCROLLBAR_SIZE, thumbY, 0xFF000000);
            }
            if(thumbY + thumbHeight < trackY + trackHeight) {
                graphics.fill(trackX, thumbY + thumbHeight, trackX + SCROLLBAR_SIZE, trackY + trackHeight, 0xFF000000);
            }
        }

        if(view.needsHorizontalScroll()) {
            int trackX = leftPos + 7;
            int trackY = horizontalScrollbarY();
            int trackWidth = inventorySizeX * 18;
            int maxScroll = view.getMaxScrollX();
            int thumbWidth = Math.max(12, trackWidth * view.getVisibleWidth() / view.getRealWidth());
            int thumbX = trackX + (maxScroll == 0 ? 0 : ((trackWidth - thumbWidth) * view.getScrollX() / maxScroll));

            if(thumbX > trackX) {
                graphics.fill(trackX, trackY, thumbX, trackY + SCROLLBAR_SIZE, 0xFF000000);
            }
            if(thumbX + thumbWidth < trackX + trackWidth) {
                graphics.fill(thumbX + thumbWidth, trackY, trackX + trackWidth, trackY + SCROLLBAR_SIZE, 0xFF000000);
            }
        }
    }

    private int verticalScrollbarX() {
        return leftPos + chestInvSizeX - SCROLLBAR_PADDING - SCROLLBAR_SIZE;
    }

    private int horizontalScrollbarY() {
        return topPos + 17 + (inventorySizeY * 18) + SCROLLBAR_PADDING;
    }

    private boolean isOverVerticalScrollbar(double mouseX, double mouseY) {
        int trackX = verticalScrollbarX();
        int trackY = topPos + 17;
        return mouseX >= trackX && mouseX < trackX + SCROLLBAR_SIZE && mouseY >= trackY && mouseY < trackY + (inventorySizeY * 18);
    }

    private boolean isOverHorizontalScrollbar(double mouseX, double mouseY) {
        int trackX = leftPos + 7;
        int trackY = horizontalScrollbarY();
        return mouseX >= trackX && mouseX < trackX + (inventorySizeX * 18) && mouseY >= trackY && mouseY < trackY + SCROLLBAR_SIZE;
    }

    private void dragVerticalScrollbar(double mouseY) {
        ScrollingContainerView view = menu.storageView;
        double ratio = Math.clamp((mouseY - (topPos + 17)) / (double) (inventorySizeY * 18), 0.0, 1.0);
        scrollTo(view.getScrollX(), (int) Math.round(ratio * view.getMaxScrollY()));
    }

    private void dragHorizontalScrollbar(double mouseX) {
        ScrollingContainerView view = menu.storageView;
        double ratio = Math.clamp((mouseX - (leftPos + 7)) / (double) (inventorySizeX * 18), 0.0, 1.0);
        scrollTo((int) Math.round(ratio * view.getMaxScrollX()), view.getScrollY());
    }

    private void scrollTo(int scrollX, int scrollY) {
        menu.setScroll(scrollX, scrollY);
        Balm.networking().sendToServer(new ServerboundScrollStoragePacket(menu.containerId, scrollX, scrollY));
    }

    private boolean isOverGrid(double mouseX, double mouseY) {
        int gridX = leftPos + 7;
        int gridY = topPos + 17;
        return mouseX >= gridX && mouseX < gridX + (inventorySizeX * 18) && mouseY >= gridY && mouseY < gridY + (inventorySizeY * 18);
    }

    // The scrollbars sit in the box's border margins, outside the grid itself, so hovering a bar
    // directly needs to scroll too, not just the grid.
    private boolean isOverScrollableArea(double mouseX, double mouseY) {
        return isOverGrid(mouseX, mouseY)
                || (menu.storageView.needsVerticalScroll() && isOverVerticalScrollbar(mouseX, mouseY))
                || (menu.storageView.needsHorizontalScroll() && isOverHorizontalScrollbar(mouseX, mouseY));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        // Only handle the wheel over the storage grid or its scrollbars - elsewhere it should fall
        // through to vanilla and other mods that hook mouseScrolled.
        if(!isOverScrollableArea(mouseX, mouseY)) {
            return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }

        ScrollingContainerView view = menu.storageView;

        // A mouse wheel only ever reports vertical motion, so hovering the horizontal bar
        // specifically is what lets you reach horizontal scroll at all.
        if(view.needsHorizontalScroll() && isOverHorizontalScrollbar(mouseX, mouseY)) {
            double delta = scrollX != 0 ? scrollX : scrollY;
            int newScrollX = Math.clamp(view.getScrollX() - (int) Math.signum(delta), 0, view.getMaxScrollX());
            if(newScrollX != view.getScrollX()) {
                scrollTo(newScrollX, view.getScrollY());
            }
            return true;
        }

        if(scrollY != 0 && view.needsVerticalScroll()) {
            int newScrollY = Math.clamp(view.getScrollY() - (int) Math.signum(scrollY), 0, view.getMaxScrollY());
            if(newScrollY != view.getScrollY()) {
                scrollTo(view.getScrollX(), newScrollY);
            }
        }

        if(scrollX != 0 && view.needsHorizontalScroll()) {
            int newScrollX = Math.clamp(view.getScrollX() - (int) Math.signum(scrollX), 0, view.getMaxScrollX());
            if(newScrollX != view.getScrollX()) {
                scrollTo(newScrollX, view.getScrollY());
            }
        }

        // Don't fall through to vanilla's own hoveredSlot-based scroll actions (eg. bundle peek):
        // scrolling remaps the view under a stationary cursor, so the hovered slot can gain an item
        // within this same input event, which isn't a real hover the player intended to act on.
        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if(event.button() == 0) {
            if(menu.storageView.needsVerticalScroll() && isOverVerticalScrollbar(event.x(), event.y())) {
                draggingVerticalScrollbar = true;
                dragVerticalScrollbar(event.y());
                return true;
            }

            if(menu.storageView.needsHorizontalScroll() && isOverHorizontalScrollbar(event.x(), event.y())) {
                draggingHorizontalScrollbar = true;
                dragHorizontalScrollbar(event.x());
                return true;
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        if(draggingVerticalScrollbar) {
            dragVerticalScrollbar(event.y());
            return true;
        }

        if(draggingHorizontalScrollbar) {
            dragHorizontalScrollbar(event.x());
            return true;
        }

        return super.mouseDragged(event, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        draggingVerticalScrollbar = false;
        draggingHorizontalScrollbar = false;

        return super.mouseReleased(event);
    }

    private void extractVoidSlot(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        //extractTab(graphics, BACKGROUND_LOCATION, leftPos+imageWidth-3, topPos, 28, 29, invCoords.x * 15, invCoords.y * 15, 7, 128, 128, true);

        blit9slice(graphics, BACKGROUND_LOCATION, leftPos+imageWidth+4, topPos, 32, 32, invCoords.x * 15, invCoords.y * 15, 7, 128, 128);

        if(fancyRendering) {
            renderSlots(graphics, storageType, leftPos+imageWidth+4 + 7, topPos + 7, 1,1);
        } else {
            renderSlots(graphics, leftPos+imageWidth+4 + 7, topPos + 7, 1,1);
        }

        graphics.blit(RenderPipelines.GUI_TEXTURED, TRASH_ICON, leftPos + imageWidth + 4 + 7 + 1, topPos +7+1, 0, 0, 16, 16, 16, 16, 16, 16, fancyRendering ? 0xFF000000 + storageType.getUiTitleColor() : -12566464);
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

    public void extractTab(GuiGraphicsExtractor guiGraphics, Identifier texture, int x, int y, int width, int height, int textureOffsetX, int textureOffsetY, int segmentSize, int textureSizeX, int textureSizeY, boolean firstTab) {
        // Top Middle
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                x, y,
                textureOffsetX+segmentSize, textureOffsetY,
                width-(segmentSize), segmentSize,
                1, segmentSize,
                textureSizeX, textureSizeY);
        // Top Right
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                x+width-segmentSize, y,
                textureOffsetX+segmentSize+1, textureOffsetY,
                segmentSize, segmentSize,
                segmentSize, segmentSize,
                textureSizeX, textureSizeY);
        // Bottom Middle
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                x+2, y+height-segmentSize,
                textureOffsetX+segmentSize, textureOffsetY+segmentSize+1,
                width-(segmentSize)-2, segmentSize,
                1, segmentSize,
                textureSizeX, textureSizeY);
        // Bottom Right
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture,
                x+width-segmentSize, y+height-segmentSize,
                textureOffsetX+segmentSize+1, textureOffsetY+segmentSize+1,
                segmentSize, segmentSize,
                segmentSize, segmentSize,
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
                x, y+segmentSize,
                textureOffsetX+segmentSize, textureOffsetY+segmentSize,
                width-(segmentSize), height-(segmentSize)-3,
                1, 1,
                textureSizeX, textureSizeY);
    }
}
