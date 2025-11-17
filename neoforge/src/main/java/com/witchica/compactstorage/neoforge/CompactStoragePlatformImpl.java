package com.witchica.compactstorage.neoforge;

import com.witchica.compactstorage.common.block.entity.CompactBarrelBlockEntity;
import com.witchica.compactstorage.common.block.entity.CompactChestBlockEntity;
import com.witchica.compactstorage.common.block.entity.DrumBlockEntity;
import com.witchica.compactstorage.common.item.BackpackItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

public class CompactStoragePlatformImpl {
    public static BlockEntityType.BlockEntitySupplier<CompactChestBlockEntity> compactChestBlockEntityProvider() {
        return CompactChestBlockEntity::new;
    }
    public static BlockEntityType.BlockEntitySupplier<CompactBarrelBlockEntity> compactBarrelBlockEntityProvider() {
        return CompactBarrelBlockEntity::new;
    }
    public static BlockEntityType.BlockEntitySupplier<DrumBlockEntity> drumBlockEntityProvider() {
        return DrumBlockEntity::new;
    }

    private static Optional<SlotResult> getCuriosSlot(Player player) {
        Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(player);

        if(curiosInventory.isPresent()) {
            ICuriosItemHandler itemHandler = curiosInventory.get();
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
