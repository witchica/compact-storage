package com.tabithastrong.compactstorage.block.entity;

import com.tabithastrong.compactstorage.CompactStorage;
import com.tabithastrong.compactstorage.screen.CompactChestScreenHandler;
import com.tabithastrong.compactstorage.util.CompactStorageInventoryImpl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@EnvironmentInterface(
    itf = ChestAnimationProgress.class,
    value = EnvType.CLIENT
)
public class CompactChestBlockEntity extends LootableContainerBlockEntity implements ExtendedScreenHandlerFactory, CompactStorageInventoryImpl, ChestAnimationProgress {
    private DefaultedList<ItemStack> inventory;

    public int inventoryWidth = 9;
    public int inventoryHeight = 3;

    public int playersUsing = 0;
    public int playersUsingOld = 0;
    public boolean isOpen = false;

    public float lidOpenness = 0f;
    public float lastLidOpenness = 0f;

    public CompactChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CompactStorage.COMPACT_CHEST_ENTITY_TYPE, blockPos, blockState);
        this.inventory = DefaultedList.ofSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
    }

    @Override
    public int size() {
        return inventoryWidth * inventoryHeight;
    }

    @Override
    public float getAnimationProgress(float delta) {
        return MathHelper.lerp(delta, lastLidOpenness, lidOpenness);
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> stackList) {
        inventory = stackList;
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.chest");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new CompactChestScreenHandler(syncId, playerInventory, (Inventory) this, inventoryWidth, inventoryHeight, Hand.MAIN_HAND);
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

    public static void tick(World world, BlockPos pos, BlockState state, CompactChestBlockEntity compactChestBlockEntity) {
        compactChestBlockEntity.lastLidOpenness = compactChestBlockEntity.lidOpenness;

        if(compactChestBlockEntity.playersUsing > 0 && compactChestBlockEntity.playersUsingOld == 0) {
            compactChestBlockEntity.isOpen = true;
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 1f, 0.9f, true);
        }else if(compactChestBlockEntity.playersUsing == 0 && compactChestBlockEntity.playersUsingOld != 0) {
            compactChestBlockEntity.isOpen = false;
        }
        
        if (compactChestBlockEntity.playersUsing == 0 && compactChestBlockEntity.lidOpenness >= 0.6f && compactChestBlockEntity.lidOpenness <= 0.7f) {
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 1f, 0.9f, true);
        }

        if(compactChestBlockEntity.isOpen && compactChestBlockEntity.lidOpenness < 0.5f) {
            compactChestBlockEntity.lidOpenness = Math.min(compactChestBlockEntity.lidOpenness += 0.25f, 1f);
        } else if(compactChestBlockEntity.isOpen && compactChestBlockEntity.lidOpenness < 1f) {
            compactChestBlockEntity.lidOpenness = Math.min(compactChestBlockEntity.lidOpenness += 0.1f, 1f);
        } else if(!compactChestBlockEntity.isOpen && compactChestBlockEntity.lidOpenness > 0.7f) {
            compactChestBlockEntity.lidOpenness = Math.max(compactChestBlockEntity.lidOpenness - 0.05f, 0f);
        } else if(!compactChestBlockEntity.isOpen && compactChestBlockEntity.lidOpenness > 0f) {
            compactChestBlockEntity.lidOpenness = Math.max(compactChestBlockEntity.lidOpenness - 0.15f, 0f);
        }

        compactChestBlockEntity.playersUsingOld = compactChestBlockEntity.playersUsing;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CompactChestScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public Text getDisplayName() {
        return getName();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }
}