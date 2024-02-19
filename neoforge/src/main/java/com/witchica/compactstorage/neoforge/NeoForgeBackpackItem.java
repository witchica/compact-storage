package com.witchica.compactstorage.neoforge;

import com.witchica.compactstorage.CompactStoragePlatform;
import com.witchica.compactstorage.common.inventory.BackpackInventoryHandlerFactory;
import com.witchica.compactstorage.common.item.BackpackItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class NeoForgeBackpackItem extends BackpackItem {
    public NeoForgeBackpackItem(Properties settings) {
        super(settings);
    }

    @Override
    public void openMenu(Player player, InteractionHand hand) {
        BackpackInventoryHandlerFactory backpackInventoryHandlerFactory = CompactStoragePlatform.getBackpackInventoryHandlerFactory(player, hand);
        player.openMenu(backpackInventoryHandlerFactory, backpackInventoryHandlerFactory::writeToByteBuf);
    }
}
