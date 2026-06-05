package com.witchica.compactstorage;

import com.mojang.datafixers.DataFixerBuilder;
import com.witchica.compactstorage.components.ResizableInventoryComponent;
import com.witchica.compactstorage.item.BackpackItem;
import com.witchica.compactstorage.mod.*;
import com.witchica.compactstorage.network.ServerboundBackpackHotkeyPacket;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.platform.event.callback.ItemCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.BlockRenameFix;
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

        ItemCallback.Tooltip.EVENT.register((itemStack, tooltip, flags) -> {
            if(itemStack.has(CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value())) {
                ResizableInventoryComponent component = itemStack.get(CompactStorageComponents.RESIZABLE_INVENTORY_DATA.value());
                if(component != null) {
                    tooltip.add(Component.translatable("tooltip.compact_storage.upgrades.resizable", component.inventoryWidth(), component.inventoryHeight()).withStyle(ChatFormatting.AQUA));
                }
            }

            if(itemStack.has(CompactStorageComponents.RETAINING_DATA.value()) && itemStack.getOrDefault(CompactStorageComponents.RETAINING_DATA.value(), false)) {
                tooltip.add(Component.translatable("tooltip.compact_storage.upgrades.retaining").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD));
            }

            if(itemStack.getItem() == CompactStorageItems.UPGRADE_RETAINING.asItem()) {
                tooltip.add(Component.translatable("tooltip.compact_storage.upgrade_retaining").withStyle(ChatFormatting.GRAY));
            }
            if(itemStack.getItem() == CompactStorageItems.UPGRADE_WIDTH.asItem()) {
                tooltip.add(Component.translatable("tooltip.compact_storage.upgrade_width").withStyle(ChatFormatting.GRAY));
            }
            if(itemStack.getItem() == CompactStorageItems.UPGRADE_HEIGHT.asItem()) {
                tooltip.add(Component.translatable("tooltip.compact_storage.upgrade_height").withStyle(ChatFormatting.GRAY));
            }
        });

        Balm.networking().registerServerboundPacket(ServerboundBackpackHotkeyPacket.TYPE, ServerboundBackpackHotkeyPacket.class, ServerboundBackpackHotkeyPacket.STREAM_CODEC, ServerboundBackpackHotkeyPacket::handle);
    }

    public static ItemStack findCuriosBackpack(Player player) {
        return Balm.modSupport().trinkets().findEquipped(player, itemStack -> itemStack.getItem() instanceof BackpackItem);
    }

    public static boolean isCuriosBackpackEquipped(Player player) {
        return Balm.modSupport().trinkets().isEquipped(player, itemStack -> itemStack.getItem() instanceof BackpackItem);
    }
}
