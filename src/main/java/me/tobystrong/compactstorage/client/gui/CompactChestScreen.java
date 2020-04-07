package me.tobystrong.compactstorage.client.gui;

import me.tobystrong.compactstorage.container.CompactChestContainer;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class CompactChestScreen extends ContainerScreen<CompactChestContainer> {

    public CompactChestScreen(CompactChestContainer container, PlayerInventory inventory, Text title) {
        super(container, inventory, title);
        this.containerHeight = 114 + container.inventoryHeight * 18;
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        this.font.draw(this.title.asFormattedString(), 8.0F, 6.0F, 4210752);
        this.font.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F, (float)(this.containerHeight - 96 + 2), 4210752);

    }

    @Override
    protected void drawBackground(float delta, int mouseX, int mouseY) {

    }
}
