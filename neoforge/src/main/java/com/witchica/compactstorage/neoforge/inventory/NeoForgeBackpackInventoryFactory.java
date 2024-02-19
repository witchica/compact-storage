package com.witchica.compactstorage.neoforge.inventory;

import com.witchica.compactstorage.common.inventory.BackpackInventoryHandlerFactory;
import com.witchica.compactstorage.common.screen.CompactChestScreenHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.IContainerFactory;

public class NeoForgeBackpackInventoryFactory extends BackpackInventoryHandlerFactory implements IContainerFactory<CompactChestScreenHandler> {
    public NeoForgeBackpackInventoryFactory(Player player, InteractionHand hand) {
        super(player, hand);
    }

    @Override
    public CompactChestScreenHandler create(int i, Inventory inventory, FriendlyByteBuf buf) {
        buf.writeInt(1);
        buf.writeInt(hand.ordinal());
        return new CompactChestScreenHandler(i, inventory, buf);
    }
}
