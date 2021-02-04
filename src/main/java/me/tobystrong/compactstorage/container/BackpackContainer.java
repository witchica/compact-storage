package me.tobystrong.compactstorage.container;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.util.CompactStorageUtilMethods;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.items.ItemStackHandler;

public class BackpackContainer extends CompactStorageBaseContainer {
    public static BackpackContainer createContainerFromItemstack(int windowID, PlayerInventory playerInventory, net.minecraft.network.PacketBuffer extraData) {
        //store the hand and the stack so we can write NBT later
        Hand hand = Hand.values()[extraData.readInt()];
        ItemStack backpack = playerInventory.player.getHeldItem(hand);

        //default width
        int inventoryWidth = 9;
        int inventoryHeight = 3;

        //if no tag, make one now to stop crashes
        if(backpack.getTag() == null) {
            backpack.setTag(new CompoundNBT());
        }

        CompoundNBT tag = backpack.getTag();

        //if it doesn't have a width, add one now - otherwise retreive it
        if(!tag.contains("width")) {
            tag.putInt("width", inventoryWidth);
            tag.putInt("height", inventoryHeight);
        } else {
            inventoryWidth = tag.getInt("width");
            inventoryHeight = tag.getInt("height");
        }

        //get a item handler from the size
        ItemStackHandler inventoryHandler = new ItemStackHandler(inventoryWidth * inventoryHeight);

        if(tag.contains("Inventory")) {
            //read in the data from the backpack
            inventoryHandler.deserializeNBT(tag.getCompound("Inventory"));
            //fix the size if need be
            inventoryHandler = CompactStorageUtilMethods.validateHandlerSize(inventoryHandler, inventoryWidth, inventoryHeight);
        }

        return new BackpackContainer(windowID, playerInventory, inventoryWidth, inventoryHeight, inventoryHandler, hand);
    }

    public Hand hand;

    public BackpackContainer(int windowID, PlayerInventory playerInventory, int inventoryWidth, int inventoryHeight, ItemStackHandler inventoryHandler, Hand hand) {
        super(CompactStorage.BACKPACK_CONTAINER_TYPE, windowID, playerInventory, inventoryWidth, inventoryHeight, inventoryHandler);
        this.hand = hand;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        //save the data to the backpack
        ItemStackHandler handler = (ItemStackHandler) this.chestInventory;
        playerIn.getHeldItem(hand).getTag().put("Inventory", handler.serializeNBT());
    }
}
