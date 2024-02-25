package com.witchica.compactstorage.forge.item;

import com.witchica.compactstorage.CompactStoragePlatform;
import com.witchica.compactstorage.common.inventory.BackpackInventoryHandlerFactory;
import com.witchica.compactstorage.common.item.BackpackItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class ForgeBackpackItem extends BackpackItem {
    public ForgeBackpackItem(Properties settings) {
        super(settings);
    }

    @Override
    public void openMenu(Player player, InteractionHand hand) {
        BackpackInventoryHandlerFactory backpackInventoryHandlerFactory = CompactStoragePlatform.getBackpackInventoryHandlerFactory(player, hand);

        if(player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(backpackInventoryHandlerFactory, backpackInventoryHandlerFactory::writeToByteBuf);
        }
    }
}
