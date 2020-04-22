package me.tobystrong.compactstorage.item;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import me.tobystrong.compactstorage.container.factory.CompactStorageContainerFactories;
import me.tobystrong.compactstorage.inventory.BackpackInventory;
import me.tobystrong.compactstorage.util.CompactStorageInventoryImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class BackpackItem extends Item implements INamedContainerProvider {
    public BackpackItem(Properties settings)
    {
        super(settings);
    }



    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity user, Hand hand) {
        
        if(!world.isRemote && hand == Hand.MAIN_HAND) {
            NetworkHooks.openGui((ServerPlayerEntity) user, this);
        }
            
        return super.onItemRightClick(world, user, hand);
    }

    public static BackpackInventory getInventory(ItemStack stack, Hand hand, PlayerEntity player) {
        if(!stack.hasTag()) {
            stack.setTag(new CompoundNBT());
        }

        if(!stack.getTag().contains("Backpack")) {
            stack.getTag().put("Backpack", new CompoundNBT());
        }
        
        return new BackpackInventory(stack.getTag().getCompound("Backpack"), hand, player);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(CompactStorage.BACKPACK_TRANSLATION_KEY);
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        ItemStack stack = playerEntity.getHeldItem(Hand.MAIN_HAND);
        CompactStorageInventoryImpl inventory = getInventory(stack, Hand.MAIN_HAND, playerEntity);

        return new CompactChestContainer(CompactStorage.BACKPACK_CONTAINER_TYPE, i, playerInventory, inventory.getInventory(), inventory.getInventoryWidth(), inventory.getInventoryHeight(), Hand.MAIN_HAND);
    }
}