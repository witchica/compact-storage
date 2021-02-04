package me.tobystrong.compactstorage.util;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class CompactStorageUtilMethods {
    public static ItemStackHandler validateHandlerSize(ItemStackHandler handler, int width, int height) {
        //check if the size matches
        if(handler.getSlots() != width * height) {
            //if not make a new handler with the right size
            ItemStackHandler newInventory = new ItemStackHandler(width * height);

            //copy everything across
            for(int x = 0; x < handler.getSlots(); x++) {
                newInventory.setStackInSlot(x, handler.getStackInSlot(x));
            }

            return newInventory;
        } else {
            return handler;
        }
    }


    public static void addInformationForUpgradableBlocks(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        int width = 9;
        int height = 3;
        boolean upgraded = false;
        boolean retaining = false;

        //this is used to store the data for placement eg: width and height
        if(stack.hasTag() && stack.getChildTag("BlockEntityTag") != null) {
            upgraded = true;

            CompoundNBT tag = stack.getChildTag("BlockEntityTag");

            //if we don't have a width default to 9, height to 3
            width = tag.contains("width") ? tag.getInt("width") : 9;
            height = tag.contains("height") ? tag.getInt("height") : 3;

            upgraded = (width != 9) && (height != 3);

            retaining = tag.contains("Inventory");
        }

        StringTextComponent widthComponent = new StringTextComponent("Width: ");
        widthComponent.append(new StringTextComponent(width + "").mergeStyle(TextFormatting.LIGHT_PURPLE));

        StringTextComponent heightComponent = new StringTextComponent("Height: ");
        heightComponent.append(new StringTextComponent(height + "").mergeStyle(TextFormatting.LIGHT_PURPLE));

        tooltip.add(widthComponent);
        tooltip.add(heightComponent);

        if(retaining) {
            tooltip.add(new StringTextComponent("Retaining").mergeStyle(TextFormatting.AQUA));
        }

        if(upgraded) {
            tooltip.add(new StringTextComponent("Upgraded").mergeStyle(TextFormatting.RED));
        }
    }

}
