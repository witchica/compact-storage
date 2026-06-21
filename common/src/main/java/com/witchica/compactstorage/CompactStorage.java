package com.witchica.compactstorage;

import com.witchica.compactstorage.block.ModBlocks;
import com.witchica.compactstorage.block.entity.ModBlockEntities;
import com.witchica.compactstorage.components.ModComponents;
import com.witchica.compactstorage.components.ResizableInventoryComponent;
import com.witchica.compactstorage.item.BackpackItem;
import com.witchica.compactstorage.item.ModItems;
import com.witchica.compactstorage.menu.ModMenuTypes;
import com.witchica.compactstorage.network.ServerboundBackpackHotkeyPacket;
import com.witchica.compactstorage.stat.ModStatistics;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.platform.event.callback.ItemCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
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

        registrars.dataComponentTypes(ModComponents::initialize);
        registrars.blocks(ModBlocks::initialize);
        registrars.items(ModItems::initializeItems);
        registrars.creativeModeTabs(ModItems::initializeCreativeTabs);
        registrars.blockEntityTypes(ModBlockEntities::initialize);
        registrars.menuTypes(ModMenuTypes::initialize);
        registrars.customStats(ModStatistics::initialize);

        ItemCallback.Tooltip.EVENT.register((itemStack, tooltip, flags) -> {
            if(itemStack.has(ModComponents.RESIZABLE_INVENTORY_DATA.value())) {
                ResizableInventoryComponent component = itemStack.get(ModComponents.RESIZABLE_INVENTORY_DATA.value());
                if(component != null) {
                    tooltip.add(Component.translatable("tooltip.compact_storage.upgrades.resizable", component.inventoryWidth(), component.inventoryHeight()).withStyle(ChatFormatting.AQUA));
                }
            }

            if(itemStack.getOrDefault(ModComponents.RETAINING_DATA.value(), false)) {
                tooltip.add(Component.translatable("tooltip.compact_storage.upgrades.retaining").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD));
            }

            if(itemStack.getOrDefault(ModComponents.VOID_SLOT.value(), false)) {
                tooltip.add(Component.translatable("tooltip.compact_storage.upgrades.void_slot").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD));
            }

            if(itemStack.has(ModComponents.ITEM_DRUM_SIZE.value())) {
                tooltip.add(Component.translatable("tooltip.compact_storage.upgrades.item_drum", itemStack.get(ModComponents.ITEM_DRUM_SIZE.value()).intValue()).withStyle(ChatFormatting.AQUA));
            }
        });

        Balm.networking().registerServerboundPacket(ServerboundBackpackHotkeyPacket.TYPE, ServerboundBackpackHotkeyPacket.class, ServerboundBackpackHotkeyPacket.STREAM_CODEC, ServerboundBackpackHotkeyPacket::handle);
    }

    public static ItemStack getEquippedBackpackStack(Player player) {
        ItemStack curiosStack = Balm.modSupport().trinkets().findEquipped(player, itemStack -> itemStack.getItem() instanceof BackpackItem);

        if(!curiosStack.isEmpty()) {
            return curiosStack;
        }

        ItemStack vanillaEquipped = player.getItemBySlot(EquipmentSlot.CHEST);

        if(vanillaEquipped.getItem() instanceof BackpackItem) {
            return vanillaEquipped;
        }

        return ItemStack.EMPTY;
    }
}
