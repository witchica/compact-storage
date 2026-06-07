package com.witchica.compactstorage.upgrades;

import com.witchica.compactstorage.data.StorageUpgrade;
import com.witchica.compactstorage.api.inventory.ItemWithResizableInventory;
import com.witchica.compactstorage.api.inventory.ResizableContainer;
import com.witchica.compactstorage.components.ResizableInventoryComponent;
import com.witchica.compactstorage.mod.CompactStorageComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Consumer;

public class ResizableUpgrade extends StorageUpgrade {
    private final ResizeType type;

    public enum ResizeType {
        WIDTH,
        HEIGHT
    }
    
    public ResizableUpgrade(String name, ResizeType type) {
        super(name);
        this.type = type;
    }

    @Override
    public DataComponentType<ResizableInventoryComponent> getDataComponent() {
        return CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value();
    }

    @Override
    public boolean applyToItemStack(ItemStack stack) {
        if(stack.has(getDataComponent()) && stack.getItem() instanceof ItemWithResizableInventory item) {
            ResizableInventoryComponent component = stack.get(getDataComponent());

            if(component != null) {
                if(type == ResizeType.WIDTH && component.inventoryWidth() < item.getMaximumWidth()) {
                    stack.update(getDataComponent(), component, ResizableInventoryComponent::increaseWidth);
                    return true;
                } else if(type == ResizeType.HEIGHT && component.inventoryHeight() < item.getMaximumHeight()) {
                    stack.update(getDataComponent(), component, ResizableInventoryComponent::increaseHeight);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean applyToBlockEntity(BlockEntity blockEntity) {
        if(blockEntity instanceof ResizableContainer resizableContainer) {
            switch (type) {
                case WIDTH: {
                    resizableContainer.setWidth(Math.min(resizableContainer.getWidth() + 1, resizableContainer.getMaximumWidth()));
                    return true;
                } case HEIGHT: {
                    resizableContainer.setHeight(Math.min(resizableContainer.getHeight() + 1, resizableContainer.getMaximumHeight()));
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isUpgradeValidForBlockEntity(BlockEntity blockEntity) {
        if(blockEntity instanceof ResizableContainer resizableContainer) {
            return (type == ResizeType.WIDTH && resizableContainer.getWidth() < resizableContainer.getMaximumWidth()) || (type == ResizeType.HEIGHT && resizableContainer.getHeight() < resizableContainer.getMaximumHeight());
        }

        return false;
    }

    @Override
    public boolean isUpgradeValidForItemStack(ItemStack stack) {
        if(stack.getItem() instanceof ItemWithResizableInventory item) {
            ResizableInventoryComponent component = stack.getOrDefault(getDataComponent(), new ResizableInventoryComponent(item.getDefaultWidth(), item.getDefaultHeight()));
            return (type == ResizeType.WIDTH && component.inventoryWidth() < item.getMaximumWidth()) || (type == ResizeType.HEIGHT && component.inventoryHeight() < item.getDefaultHeight());
        }

        return false;
    }

    @Override
    public Component getFailedUpgradeMessage() {
        return Component.translatable("message.compact_storage.upgrades.width.fail").withStyle(ChatFormatting.RED);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        tooltipAdder.accept(Component.translatable(type == ResizeType.WIDTH ? "tooltip.compact_storage.upgrade_width" : "tooltip.compact_storage.upgrade_height").withStyle(ChatFormatting.GRAY));
    }
}
