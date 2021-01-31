package me.tobystrong.compactstorage.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class CompactChestContainerScreen extends ContainerScreen<CompactChestContainer> {
    public CompactChestContainerScreen(CompactChestContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        renderBackground(matrixStack);
    }
}
