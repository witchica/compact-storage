package com.witchica.compactstorage;

import com.witchica.compactstorage.item.BackpackItem;
import com.witchica.compactstorage.mod.*;
import com.witchica.compactstorage.network.ServerboundBackpackHotkeyPacket;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompactStorage {

    public static final Logger logger = LoggerFactory.getLogger(CompactStorage.class);

    public static final String MOD_ID = "compact_storage";

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static CompactStorageConfig config() {
        return Balm.config().getActiveConfig(CompactStorageConfig.class);
    }

    public static void initialize(BalmRegistrars registrars) {
        Balm.config().registerConfig(CompactStorageConfig.class);

        registrars.dataComponentTypes(CompactStorageComponents::initialize);
        registrars.blocks(CompactStorageBlocks::initialize);
        registrars.items(CompactStorageItems::initializeItems);
        registrars.creativeModeTabs(CompactStorageItems::initializeCreativeTabs);
        registrars.blockEntityTypes(CompactStorageBlockEntities::initialize);
        registrars.menuTypes(CompactStorageMenuTypes::initialize);

        Balm.networking().registerServerboundPacket(ServerboundBackpackHotkeyPacket.TYPE, ServerboundBackpackHotkeyPacket.class, ServerboundBackpackHotkeyPacket.STREAM_CODEC, ServerboundBackpackHotkeyPacket::handle);
    }

    public static ItemStack findCuriosBackpack(Player player) {
        return Balm.modSupport().trinkets().findEquipped(player, itemStack -> itemStack.getItem() instanceof BackpackItem);
    }

    public static boolean isCuriosBackpackEquipped(Player player) {
        return Balm.modSupport().trinkets().isEquipped(player, itemStack -> itemStack.getItem() instanceof BackpackItem);
    }
}
