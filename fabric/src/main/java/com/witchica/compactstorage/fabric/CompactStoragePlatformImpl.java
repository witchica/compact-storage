package com.witchica.compactstorage.fabric;

import com.witchica.compactstorage.common.block.entity.CompactBarrelBlockEntity;
import com.witchica.compactstorage.common.block.entity.CompactChestBlockEntity;
import com.witchica.compactstorage.common.block.entity.DrumBlockEntity;
import com.witchica.compactstorage.common.item.BackpackItem;
import com.witchica.compactstorage.fabric.block.entity.FabricCompactBarrelBlockEntity;
import com.witchica.compactstorage.fabric.block.entity.FabricCompactChestBlockEntity;
import com.witchica.compactstorage.fabric.block.entity.FabricDrumBlockEntity;
import dev.emi.trinkets.api.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.List;
import java.util.Optional;

public class CompactStoragePlatformImpl {
    public static BlockEntityType.BlockEntitySupplier<CompactChestBlockEntity> compactChestBlockEntityProvider() {
        return FabricCompactChestBlockEntity::new;
    }
    public static BlockEntityType.BlockEntitySupplier<CompactBarrelBlockEntity> compactBarrelBlockEntityProvider() {
        return FabricCompactBarrelBlockEntity::new;
    }
    public static BlockEntityType.BlockEntitySupplier<DrumBlockEntity> drumBlockEntityProvider() {
        return FabricDrumBlockEntity::new;
    }

    public static Optional<ItemStack> getAdditionalSlotBackpack(Player player) {
        if(FabricLoader.getInstance().isModLoaded("trinkets")) {
            Optional<TrinketComponent> trinketComponentOptional = TrinketsApi.getTrinketComponent(player);

            if (trinketComponentOptional.isPresent()) {
                TrinketComponent trinketComponent = trinketComponentOptional.get();
                List<Tuple<SlotReference, ItemStack>> slots = trinketComponent.getEquipped(itemStack -> itemStack.getItem() instanceof BackpackItem);

                if (!slots.isEmpty()) {
                    return Optional.of(slots.get(0).getB());
                }
            }
        }

        return Optional.empty();
    }

    public static Optional<ItemStack> getBackpackToRender(Player player) {
        return getAdditionalSlotBackpack(player);
    }
}
