package com.witchica.compactstorage.fabric.datagen;

import com.witchica.compactstorage.CompactStorage;
import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.mod.CompactStorageBlocks;
import com.witchica.compactstorage.mod.CompactStorageItemTags;
import com.witchica.compactstorage.mod.CompactStorageItems;
import net.blay09.mods.balm.world.item.DeferredItem;
import net.blay09.mods.balm.world.level.block.DeferredBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ModAdvancementProvider extends FabricAdvancementProvider {
    protected ModAdvancementProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
        HolderLookup.RegistryLookup<Item> items = registryLookup.lookupOrThrow(Registries.ITEM);
        AdvancementHolder compactingYourStorage = Advancement.Builder.advancement()
                .display(CompactStorageBlocks.compactChests.get(StorageType.ACACIA),
                        Component.translatable("advancement.compact_storage.compacting_your_storage.title"),
                        Component.translatable("advancement.compact_storage.compacting_your_storage.description"),
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("got_chest", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items, CompactStorageItemTags.COMPACT_CHESTS))).save(consumer, "compact_storage:compacting_your_storage");


        AdvancementHolder gotBarrel = Advancement.Builder.advancement()
                .display(CompactStorageBlocks.compactBarrels.get(StorageType.RED),
                        Component.translatable("advancement.compact_storage.got_barrel.title"),
                        Component.translatable("advancement.compact_storage.got_barrel.description"),
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .parent(compactingYourStorage)
                .addCriterion("got_barrel", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items, CompactStorageItemTags.COMPACT_BARRELS))).save(consumer, "compact_storage:got_barrel");

        AdvancementHolder gotDrum = Advancement.Builder.advancement()
                .display(CompactStorageBlocks.itemDrums.get(StorageType.PALE_OAK),
                        Component.translatable("advancement.compact_storage.got_drum.title"),
                        Component.translatable("advancement.compact_storage.got_drum.description"),
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .parent(compactingYourStorage)
                .addCriterion("got_drum", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items, CompactStorageItemTags.ITEM_DRUMS))).save(consumer, "compact_storage:got_drum");

        AdvancementHolder gotBackpack = Advancement.Builder.advancement()
                .display(CompactStorageItems.BACKPACK_ITEMS.get(StorageType.PURPLE),
                        Component.translatable("advancement.compact_storage.got_backpack.title"),
                        Component.translatable("advancement.compact_storage.got_backpack.description"),
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .parent(compactingYourStorage)
                .addCriterion("got_backpack", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items, CompactStorageItemTags.STORAGE_ITEMS))).save(consumer, "compact_storage:got_backpack");
    }
}
