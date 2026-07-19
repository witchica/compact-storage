package com.witchica.compactstorage;

import com.witchica.compactstorage.api.inventory.ResizableContainer;
import com.witchica.compactstorage.api.inventory.RetainingContainer;
import com.witchica.compactstorage.api.inventory.VoidSlotProvider;
import com.witchica.compactstorage.block.ModBlocks;
import com.witchica.compactstorage.block.entity.ModBlockEntities;
import com.witchica.compactstorage.block.entity.base.BaseItemDrumBlockEntity;
import com.witchica.compactstorage.components.ModComponents;
import com.witchica.compactstorage.components.ResizableInventoryComponent;
import com.witchica.compactstorage.integration.BaseBalmIntegration;
import com.witchica.compactstorage.integration.CompactStorageTrinketsSupport;
import com.witchica.compactstorage.item.ModItems;
import com.witchica.compactstorage.menu.ModMenuTypes;
import com.witchica.compactstorage.network.ServerboundBackpackHotkeyPacket;
import com.witchica.compactstorage.network.ServerboundScrollStoragePacket;
import com.witchica.compactstorage.stat.ModStatistics;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.platform.event.callback.ItemCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class CompactStorage {
    public static final Logger logger = LoggerFactory.getLogger(CompactStorage.class);
    public static final String MOD_ID = "compact_storage";
    private static Supplier<CompactStorageTrinketsSupport> trinkets;

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
        Balm.networking().registerServerboundPacket(ServerboundScrollStoragePacket.TYPE, ServerboundScrollStoragePacket.class, ServerboundScrollStoragePacket.STREAM_CODEC, ServerboundScrollStoragePacket::handle);

        trinkets = Balm.getRuntime().<CompactStorageTrinketsSupport>modProxy()
                .with("trinkets_updated", "com.witchica.compactstorage.integration.TrinketsUpdatedModSupport")
                .withFallback(new BaseBalmIntegration())
                .buildLazily();

        Balm.modSupport().hudInfo().registerGlobalBlockInfo(id("chest_barrel_info"), (context, output) -> {
            if(context.blockEntity() instanceof ResizableContainer resizableContainer) {
                output.text(Component.translatable("tooltip.compact_storage.upgrades.resizable", resizableContainer.getWidth(), resizableContainer.getHeight()).withStyle(ChatFormatting.AQUA));
            }

            if(context.blockEntity() instanceof BaseItemDrumBlockEntity itemDrum) {
                output.progress((float) itemDrum.clientStoredItems / (itemDrum.clientStackSize * itemDrum.getSize()));
            }

            if(context.blockEntity() instanceof RetainingContainer retainingContainer && retainingContainer.isRetaining()) {
                output.text(Component.translatable("tooltip.compact_storage.upgrades.retaining").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD));
            }
            if(context.blockEntity() instanceof VoidSlotProvider voidSlotProvider && voidSlotProvider.hasVoidSlot()) {
                output.text(Component.translatable("tooltip.compact_storage.upgrades.void_slot").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD));
            }
        });
    }

    public static CompactStorageTrinketsSupport getTrinkets() {
        return trinkets.get();
    }

    public static ItemStack getEquippedBackpackStack(Player player) {
        return getTrinkets().getEquippedBackpackStack(player);
    }
}
