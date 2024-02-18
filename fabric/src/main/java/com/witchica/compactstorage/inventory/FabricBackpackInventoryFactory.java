package com.witchica.compactstorage.inventory;

import com.witchica.compactstorage.common.inventory.BackpackInventoryHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class FabricBackpackInventoryFactory extends BackpackInventoryHandlerFactory implements ExtendedScreenHandlerFactory {
    public FabricBackpackInventoryFactory(Player player, InteractionHand hand) {
        super(player, hand);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        buf.writeInt(1);
        buf.writeInt(hand.ordinal());
    }
}
