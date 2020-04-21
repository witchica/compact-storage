package me.tobystrong.compactstorage.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import me.tobystrong.compactstorage.container.CompactChestContainer;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CompactChestScreen extends ContainerScreen<CompactChestContainer> {
    public static final Identifier CHEST_SLOTS_TEXTURE = new Identifier("compact-storage", "textures/gui/chest_slots.png");
    public static final Identifier CHEST_BACKGROUND_TEXTURE = new Identifier("compact-storage", "textures/gui/chest.png");
    
    private CompactChestContainer container;

    public CompactChestScreen(CompactChestContainer container, PlayerInventory inventory, Text title) {
        super(container, inventory, title);
        this.container = container;

        this.containerWidth = 14 + container.inventoryWidth * 18;
        this.containerHeight = 114 + container.inventoryHeight * 18 + 7;
    }
    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        this.font.draw(this.title.asFormattedString(), 8.0F, 6.0F, 4210752);
        this.font.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F, (float)(this.containerHeight - 96 - 3), 4210752);

        super.drawForeground(mouseX, mouseY);
    }

    @Override
    protected void drawBackground(float delta, int mouseX, int mouseY) {
        this.renderBackground();
        RenderSystem.disableLighting();

        minecraft.getTextureManager().bindTexture(CHEST_BACKGROUND_TEXTURE);

        //blit(x, y, width, height, u, v, uWidth, vHeight, texWidth, texHeight);
        //corners


        blit(x, y, 0, 0, 7, 7, 15, 15);
        blit(x + containerWidth - 7, y, 8, 0, 7, 7, 15, 15);

        blit(x, y + containerHeight - 7, 0, 8, 8, 7, 15, 15);
        blit(x + containerWidth - 7, y + containerHeight - 7, 8, 8, 7, 7, 15, 15);

        //middle bit
        blit(x + 7, y + 7, containerWidth - 14, containerHeight - 14, 7, 7, 1, 1, 15, 15);

        //left side
        blit(x, y + 7, 7, containerHeight - 14, 0, 7, 7, 1, 15, 15);

        //right side
        blit(x + containerWidth - 7, y + 7, 7, containerHeight - 14, 8, 7, 7, 1, 15, 15);

        //top
        blit(x + 7, y, containerWidth - 14, 7, 7, 0, 1, 7, 15, 15);

        //bottom
        blit(x + 7, y + containerHeight - 7, containerWidth - 14, 7, 7, 8, 1, 7, 15, 15);

        minecraft.getTextureManager().bindTexture(CHEST_SLOTS_TEXTURE);
        //chest slots
        blit(this.x + 7, this.y + 17, 0, 0, 18 * container.inventoryWidth, 18 * container.inventoryHeight, 432, 216);
        //inv slots  
        blit(this.x + (containerWidth / 2) - 9 * 9, this.y + (container.inventoryHeight * 18) + 18 + 17, 0, 0, 18 * 9, 18 * 3, 432, 216);
        //hotbar slots
        blit(this.x + (containerWidth / 2) - 9 * 9, this.y + (container.inventoryHeight * 18) + 18 + 60 + 17, 0, 0, 18 * 9, 18 * 1, 432, 216);   
    }
}
