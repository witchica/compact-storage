package com.tabithastrong.compactstorage.block.entity;

import com.tabithastrong.compactstorage.CompactStorage;
import com.tabithastrong.compactstorage.block.CompactBarrelBlock;
import com.tabithastrong.compactstorage.screen.CompactChestScreenHandler;
import com.tabithastrong.compactstorage.util.CompactStorageInventoryImpl;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class CompactBarrelBlockEntity extends LootableContainerBlockEntity implements ExtendedScreenHandlerFactory, CompactStorageInventoryImpl {
    private DefaultedList<ItemStack> inventory;

    public int inventoryWidth = 9;
    public int inventoryHeight = 6;

    public int playersUsing = 0;
    public int playersUsingOld = 0;
    public boolean isOpen = false;

    public InventoryStorage inventoryStorage = InventoryStorage.of(this, null);
    public CompactBarrelBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CompactStorage.COMPACT_BARREL_ENTITY_TYPE, blockPos, blockState);
        this.inventory = DefaultedList.ofSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
    }

    @Override
    public int size() {
        return inventoryWidth * inventoryHeight;
    }

    
    protected DefaultedList<ItemStack> getInvStackList() {
        return inventory;
    }

    @Override
    protected DefaultedList<ItemStack> method_11282() {
        return inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> stackList) {
        inventory = stackList;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.barrel");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new CompactChestScreenHandler(syncId, playerInventory, writeToByteBuf());
    }

    @Override
    public int getInventoryWidth() {
        return inventoryWidth;
    }

    @Override
    public int getInventoryHeight() {
        return inventoryHeight;
    }

    @Override
    public void onOpen(PlayerEntity player) {
        super.onOpen(player);

        if(!player.isSpectator()) {
            playersUsing += 1;
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        super.onClose(player);

        if(!player.isSpectator()) {
            playersUsing -= 1;
        }
    }

    public void resizeInventory(boolean copy_contents) {
        DefaultedList<ItemStack> newInventory = DefaultedList.ofSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);

        if(copy_contents) {
            DefaultedList<ItemStack> list = this.inventory;

            for(int i = 0; i < list.size(); i++) {
                newInventory.set(i, list.get(i));
            }
        }

        this.inventory = newInventory;
        inventoryStorage = InventoryStorage.of(this, null);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.inventoryWidth = nbt.contains("inventory_width") ? nbt.getInt("inventory_width") : 9;
        this.inventoryHeight = nbt.contains("inventory_height") ? nbt.getInt("inventory_height") : 3;

        this.inventory = DefaultedList.ofSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
        readItemsFromTag(inventory, nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        writeItemsToTag(inventory, nbt);

        nbt.putInt("inventory_width", inventoryWidth);
        nbt.putInt("inventory_height", inventoryHeight);
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public static void tick(World world, BlockPos pos, BlockState state, CompactBarrelBlockEntity compactChestBlockEntity) {

        if(compactChestBlockEntity.playersUsing > 0 && compactChestBlockEntity.playersUsingOld == 0) {
            compactChestBlockEntity.isOpen = true;
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_BARREL_OPEN, SoundCategory.BLOCKS, 1f, 0.9f, true);
            world.setBlockState(pos, state.with(CompactBarrelBlock.OPEN, true));
        }else if(compactChestBlockEntity.playersUsing == 0 && compactChestBlockEntity.playersUsingOld != 0) {
            compactChestBlockEntity.isOpen = false;
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.BLOCKS, 1f, 0.9f, true);
            world.setBlockState(pos, state.with(CompactBarrelBlock.OPEN, false));
        }

        compactChestBlockEntity.playersUsingOld = compactChestBlockEntity.playersUsing;
    }

    public PacketByteBuf writeToByteBuf() {
        PacketByteBuf packetByteBuf = PacketByteBufs.create();
        packetByteBuf.writeInt(0);
        packetByteBuf.writeBlockPos(getPos());

        return packetByteBuf;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CompactChestScreenHandler(syncId, playerInventory, writeToByteBuf());
    }

    @Override
    public Text getDisplayName() {
        return getName();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeInt(0);
        buf.writeBlockPos(this.pos);
    }

    public boolean increaseSize(int x, int y) {
        if((inventoryWidth > 23 && x > 0) || (inventoryHeight > 11 && y > 0)) {
            return false;
        }

        inventoryWidth += x;
        inventoryHeight += y;

        resizeInventory(true);
        markDirty();
        world.updateListeners(pos, getCachedState(), getCachedState(), 1);

        return true;
    }
}