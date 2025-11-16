package com.witchica.compactstorage.forge;

import com.witchica.compactstorage.common.block.entity.CompactBarrelBlockEntity;
import com.witchica.compactstorage.common.block.entity.CompactChestBlockEntity;
import com.witchica.compactstorage.common.block.entity.DrumBlockEntity;
import com.witchica.compactstorage.common.inventory.BackpackInventoryHandlerFactory;
import com.witchica.compactstorage.common.item.BackpackItem;
import com.witchica.compactstorage.forge.block.entity.ForgeCompactBarrelBlockEntity;
import com.witchica.compactstorage.forge.block.entity.ForgeCompactChestBlockEntity;
import com.witchica.compactstorage.forge.block.entity.ForgeDrumBlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.List;
import java.util.Optional;

public class CompactStoragePlatformImpl {
    public static BlockEntityType.BlockEntitySupplier<CompactChestBlockEntity> compactChestBlockEntityProvider() {
       return ForgeCompactChestBlockEntity::new;
    }
    public static BlockEntityType.BlockEntitySupplier<CompactBarrelBlockEntity> compactBarrelBlockEntityProvider() {
        return ForgeCompactBarrelBlockEntity::new;
    }
    public static BlockEntityType.BlockEntitySupplier<DrumBlockEntity> drumBlockEntityProvider() {
        return ForgeDrumBlockEntity::new;
    }

    private static Optional<SlotResult> getCuriosSlot(Player player) {
        LazyOptional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);

        if(curiosInventory.isPresent()) {
            ICuriosItemHandler itemHandler = curiosInventory.resolve().get();
            Optional<SlotResult> slotResult = itemHandler.findFirstCurio(itemStack -> itemStack.getItem() instanceof BackpackItem);

            if (slotResult.isPresent()) {
                return slotResult;
            }
        }

        return Optional.empty();
    }

    public static Optional<ItemStack> getAdditionalSlotBackpack(Player player) {
        if(ModList.get().isLoaded("curios")) {
            Optional<SlotResult> slotResult = getCuriosSlot(player);
            return slotResult.map(SlotResult::stack);
        }

        return Optional.empty();

    }

    public static Optional<ItemStack> getBackpackToRender(Player player) {
        if(ModList.get().isLoaded("curios")) {
            Optional<SlotResult> slotResult = getCuriosSlot(player);

            if(slotResult.isPresent() && slotResult.get().slotContext().visible()) {
                return slotResult.map(SlotResult::stack);
            }
        }

        return Optional.empty();
    }
}
