package me.tobystrong.compactstorage.item;

import java.util.List;

import com.mojang.datafixers.Typed;

import me.tobystrong.compactstorage.CompactStorage;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ChestUpgradeItem extends Item {
    public ChestUpgradeItem(Settings settings) 
    {
		super(settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        ItemStack stack = user.getStackInHand(hand);

        //backpack logic here
        if(!world.isClient && hand == Hand.MAIN_HAND && user.getStackInHand(Hand.OFF_HAND).getItem() == CompactStorage.BACKPACK) {
            ItemStack backpack = user.getStackInHand(Hand.OFF_HAND);
            ItemStack upgrade = user.getStackInHand(hand);

            if(upgrade.getItem() == CompactStorage.CHEST_UPGRADE_ROW) {
                int inventory_width = backpack.getTag().getCompound("Backpack").getInt("inventory_width");

                if(inventory_width < 24) {
                    backpack.getTag().getCompound("Backpack").putInt("inventory_width", backpack.getTag().getCompound("Backpack").getInt("inventory_width") + 1);

                    user.setStackInHand(Hand.OFF_HAND, backpack);
                    user.getStackInHand(hand).decrement(1);
                    
                    user.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1f, 1f);
                    return TypedActionResult.consume(user.getStackInHand(hand));
                } else {
                    user.sendMessage(new TranslatableText("tooltip.compact-storage.fully_upgraded_row"));
                }
            } else if(upgrade.getItem() == CompactStorage.CHEST_UPGRADE_COLUMN) {
                int inventory_height = backpack.getTag().getCompound("Backpack").getInt("inventory_height");

                if(inventory_height < 12) {
                    backpack.getTag().getCompound("Backpack").putInt("inventory_height", backpack.getTag().getCompound("Backpack").getInt("inventory_height") + 1);
                    
                    user.setStackInHand(Hand.OFF_HAND, backpack);
                    user.getStackInHand(hand).decrement(1);

                    user.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1f, 1f);
                    return TypedActionResult.consume(user.getStackInHand(hand));
                } else {
                    user.sendMessage(new TranslatableText("tooltip.compact-storage.fully_upgraded_column"));
                }
            }
        } else if(!world.isClient && user.getStackInHand(Hand.OFF_HAND).getItem() != CompactStorage.BACKPACK) {
            user.sendMessage(new TranslatableText("tooltip.compact-storage.upgrade_error"));
        }

        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        if(stack.getItem() == CompactStorage.CHEST_UPGRADE_ROW) {
            tooltip.add(new TranslatableText("tooltip.compact-storage.row_upgrade_descriptor").setStyle(new Style().setColor(Formatting.GRAY)));
        } else if(stack.getItem() == CompactStorage.CHEST_UPGRADE_COLUMN) {
            tooltip.add(new TranslatableText("tooltip.compact-storage.column_upgrade_descriptor").setStyle(new Style().setColor(Formatting.GRAY)));
        }

        tooltip.add(new TranslatableText("tooltip.compact-storage.upgrade_backpack").setStyle(new Style().setColor(Formatting.GRAY)));
    }
}