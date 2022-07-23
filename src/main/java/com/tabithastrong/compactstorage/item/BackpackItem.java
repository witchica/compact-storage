package com.tabithastrong.compactstorage.item;

import com.tabithastrong.compactstorage.CompactStorage;
import com.tabithastrong.compactstorage.inventory.BackpackInventory;
import com.tabithastrong.compactstorage.inventory.BackpackInventoryHandlerFactory;
import com.tabithastrong.compactstorage.screen.CompactChestScreenHandler;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BackpackItem extends Item {
    public BackpackItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            user.openHandledScreen(new BackpackInventoryHandlerFactory(user, hand));
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        int inventoryX = 9;
        int inventoryY = 6;

        if(stack.hasNbt() && stack.getNbt().contains("Backpack")) {
            inventoryX = stack.getNbt().getCompound("Backpack").getInt("inventory_width");
            inventoryY = stack.getNbt().getCompound("Backpack").getInt("inventory_height");
        }

        int slots = inventoryX * inventoryY;

        tooltip.add(Text.translatable("text.compact_storage.tooltip.size_x", inventoryX).formatted(Formatting.GRAY, Formatting.ITALIC));
        tooltip.add(Text.translatable("text.compact_storage.tooltip.size_y", inventoryY).formatted(Formatting.GRAY, Formatting.ITALIC));
        tooltip.add(Text.translatable("text.compact_storage.tooltip.slots", slots).formatted(Formatting.DARK_PURPLE, Formatting.ITALIC));
    }
}
