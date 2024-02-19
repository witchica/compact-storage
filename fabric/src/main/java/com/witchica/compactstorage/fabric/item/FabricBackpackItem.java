package com.witchica.compactstorage.fabric.item;

import com.witchica.compactstorage.CompactStoragePlatform;
import com.witchica.compactstorage.common.item.BackpackItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class FabricBackpackItem extends BackpackItem {
    public FabricBackpackItem(Properties settings) {
        super(settings);
    }

    @Override
    public void openMenu(Player player, InteractionHand hand) {
        player.openMenu(CompactStoragePlatform.getBackpackInventoryHandlerFactory(player, hand));
    }
}
