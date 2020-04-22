package me.tobystrong.compactstorage.item;

import me.tobystrong.compactstorage.CompactStorage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;

public class ChestUpgradeItem extends Item {
    public ChestUpgradeItem(Properties settings)
    {
		super(settings);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity user, Hand hand)
    {
        ItemStack stack = user.getHeldItem(hand);

        //backpack logic here
        if(!world.isRemote && hand == Hand.MAIN_HAND && user.getHeldItem(Hand.OFF_HAND).getItem() == CompactStorage.BACKPACK) {
            ItemStack backpack = user.getHeldItem(Hand.OFF_HAND);
            ItemStack upgrade = user.getHeldItem(hand);

            if(upgrade.getItem() == CompactStorage.CHEST_UPGRADE_ROW) {
                int inventory_width = backpack.getTag().getCompound("Backpack").getInt("inventory_width");

                if(inventory_width < 24) {
                    backpack.getTag().getCompound("Backpack").putInt("inventory_width", backpack.getTag().getCompound("Backpack").getInt("inventory_width") + 1);

                    user.setHeldItem(Hand.OFF_HAND, backpack);
                    user.getHeldItem(hand).shrink(1);
                    
                    user.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1f, 1f);
                    return ActionResult.resultConsume(user.getHeldItem(hand));
                } else {
                    user.sendMessage(new TranslationTextComponent("tooltip.compact-storage.fully_upgraded_row"));
                }
            } else if(upgrade.getItem() == CompactStorage.CHEST_UPGRADE_COLUMN) {
                int inventory_height = backpack.getTag().getCompound("Backpack").getInt("inventory_height");

                if(inventory_height < 12) {
                    backpack.getTag().getCompound("Backpack").putInt("inventory_height", backpack.getTag().getCompound("Backpack").getInt("inventory_height") + 1);
                    
                    user.setHeldItem(Hand.OFF_HAND, backpack);
                    user.getHeldItem(hand).shrink(1);

                    user.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1f, 1f);
                    return ActionResult.resultConsume(user.getHeldItem(hand));
                } else {
                    user.sendMessage(new TranslationTextComponent("tooltip.compact-storage.fully_upgraded_column"));
                }
            }
        } else if(!world.isRemote && user.getHeldItem(Hand.OFF_HAND).getItem() != CompactStorage.BACKPACK) {
            user.sendMessage(new TranslationTextComponent("tooltip.compact-storage.upgrade_error"));
        }

        return ActionResult.resultConsume(user.getHeldItem(hand));
    }


    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag context) {
        super.addInformation(stack, world, tooltip, context);

        if(stack.getItem() == CompactStorage.CHEST_UPGRADE_ROW) {
            tooltip.add(new TranslationTextComponent("tooltip.compact-storage.row_upgrade_descriptor").setStyle(new Style().setColor(TextFormatting.GRAY)));
        } else if(stack.getItem() == CompactStorage.CHEST_UPGRADE_COLUMN) {
            tooltip.add(new TranslationTextComponent("tooltip.compact-storage.column_upgrade_descriptor").setStyle(new Style().setColor(TextFormatting.GRAY)));
        }

        tooltip.add(new TranslationTextComponent("tooltip.compact-storage.upgrade_backpack").setStyle(new Style().setColor(TextFormatting.GRAY)));
    }
}