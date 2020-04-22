package me.tobystrong.compactstorage.container.factory;

import me.tobystrong.compactstorage.CompactStorage;
import me.tobystrong.compactstorage.block.CompactChestBlock;
import me.tobystrong.compactstorage.block.entity.CompactChestBlockEntity;
import me.tobystrong.compactstorage.container.CompactChestContainer;
import me.tobystrong.compactstorage.item.BackpackItem;
import me.tobystrong.compactstorage.util.CompactStorageInventoryImpl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.IContainerFactory;

import javax.annotation.Nullable;

public class CompactStorageContainerFactories
{
    public static class BackpackContainerFactory implements IContainerFactory<CompactChestContainer> {
        @Override
        public CompactChestContainer create(int syncId, PlayerInventory playerInventory, PacketBuffer extraData) {
            PlayerEntity player = playerInventory.player;
            Hand hand = Hand.MAIN_HAND;
            ItemStack backpack = player.getHeldItem(hand);
            final CompactStorageInventoryImpl inventory = BackpackItem.getInventory(backpack, hand, player);

            return new CompactChestContainer(CompactStorage.BACKPACK_CONTAINER_TYPE, syncId, player.inventory, inventory.getInventory(), inventory.getInventoryWidth(), inventory.getInventoryHeight(), hand);
        }
    }

    public static class ChestContainerFactory implements IContainerFactory<CompactChestContainer> {
        @Override
        public CompactChestContainer create(int syncId, PlayerInventory playerInventory, PacketBuffer extraData) {
            final World world = playerInventory.player.world;
            final BlockPos pos = extraData.readBlockPos();

            if(world.getTileEntity(pos) instanceof CompactChestBlockEntity) {
                return (CompactChestContainer) ((CompactChestBlockEntity) world.getTileEntity(pos)).createMenu(syncId, playerInventory, playerInventory.player);
            }

            return null;
        }
    }
}
