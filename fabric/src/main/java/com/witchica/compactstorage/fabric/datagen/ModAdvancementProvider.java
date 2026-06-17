package com.witchica.compactstorage.fabric.datagen;

import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.mod.CompactStorageBlocks;
import com.witchica.compactstorage.mod.CompactStorageItemTags;
import com.witchica.compactstorage.mod.CompactStorageItems;
import com.witchica.compactstorage.mod.CompactStorageUpgrades;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.PlayerPredicate;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

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
                .addCriterion("got_storage", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items, CompactStorageItemTags.STORAGE_BLOCKS))).save(consumer, "compact_storage:compacting_your_storage");

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

        HolderLookup.RegistryLookup<Identifier> statLookup = registryLookup.lookupOrThrow(Registries.CUSTOM_STAT);

        AdvancementHolder usedWidthUpgrade = Advancement.Builder.advancement()
                .display(CompactStorageUpgrades.WIDTH_UPGRADE.getItem(),
                        Component.translatable("advancement.compact_storage.width_upgrade.title"),
                        Component.translatable("advancement.compact_storage.width_upgrade.description"),
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .parent(compactingYourStorage)
                .addCriterion("stat_updated",
                        PlayerTrigger.TriggerInstance.located(
                                EntityPredicate.Builder.entity()
                                        .player(PlayerPredicate.Builder.player()
                                                .addStat(Stats.CUSTOM, statLookup.getOrThrow(ResourceKey.create(Registries.CUSTOM_STAT, CompactStorageUpgrades.WIDTH_UPGRADE.getStatisticIdentifier())), MinMaxBounds.Ints.atLeast(1)).build()))).save(consumer, "compact_storage:used_width_upgrade");

        AdvancementHolder usedHeightUpgrade = Advancement.Builder.advancement()
                .display(CompactStorageUpgrades.HEIGHT_UPGRADE.getItem(),
                        Component.translatable("advancement.compact_storage.height_upgrade.title"),
                        Component.translatable("advancement.compact_storage.height_upgrade.description"),
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .parent(usedWidthUpgrade)
                .addCriterion("stat_updated",
                        PlayerTrigger.TriggerInstance.located(
                                EntityPredicate.Builder.entity()
                                        .player(PlayerPredicate.Builder.player()
                                                .addStat(Stats.CUSTOM, statLookup.getOrThrow(ResourceKey.create(Registries.CUSTOM_STAT, CompactStorageUpgrades.HEIGHT_UPGRADE.getStatisticIdentifier())), MinMaxBounds.Ints.atLeast(1)).build()))).save(consumer, "compact_storage:used_height_upgrade");

        AdvancementHolder usedItemDrumUpgrade = Advancement.Builder.advancement()
                .display(CompactStorageUpgrades.ITEM_DRUM_UPGRADE.getItem(),
                        Component.translatable("advancement.compact_storage.item_drum_upgrade.title"),
                        Component.translatable("advancement.compact_storage.item_drum_upgrade.description"),
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .parent(gotDrum)
                .addCriterion("stat_updated",
                        PlayerTrigger.TriggerInstance.located(
                                EntityPredicate.Builder.entity()
                                        .player(PlayerPredicate.Builder.player()
                                                .addStat(Stats.CUSTOM, statLookup.getOrThrow(ResourceKey.create(Registries.CUSTOM_STAT, CompactStorageUpgrades.ITEM_DRUM_UPGRADE.getStatisticIdentifier())), MinMaxBounds.Ints.atLeast(1)).build()))).save(consumer, "compact_storage:used_item_drum_upgrade");

        AdvancementHolder usedVoidSlotUpgrade = Advancement.Builder.advancement()
                .display(CompactStorageUpgrades.VOID_SLOT_UPGRADE.getItem(),
                        Component.translatable("advancement.compact_storage.void_slot_upgrade.title"),
                        Component.translatable("advancement.compact_storage.void_slot_upgrade.description"),
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .parent(compactingYourStorage)
                .addCriterion("stat_updated",
                        PlayerTrigger.TriggerInstance.located(
                                EntityPredicate.Builder.entity()
                                        .player(PlayerPredicate.Builder.player()
                                                .addStat(Stats.CUSTOM, statLookup.getOrThrow(ResourceKey.create(Registries.CUSTOM_STAT, CompactStorageUpgrades.VOID_SLOT_UPGRADE.getStatisticIdentifier())), MinMaxBounds.Ints.atLeast(1)).build()))).save(consumer, "compact_storage:used_void_slot_upgrade");

        AdvancementHolder usedRetainingUpgrade = Advancement.Builder.advancement()
                .display(CompactStorageUpgrades.RETAINING_UPGRADE.getItem(),
                        Component.translatable("advancement.compact_storage.retainer_upgrade.title"),
                        Component.translatable("advancement.compact_storage.retainer_upgrade.description"),
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .parent(compactingYourStorage)
                .addCriterion("stat_updated",
                        PlayerTrigger.TriggerInstance.located(
                                EntityPredicate.Builder.entity()
                                        .player(PlayerPredicate.Builder.player()
                                                .addStat(Stats.CUSTOM, statLookup.getOrThrow(ResourceKey.create(Registries.CUSTOM_STAT, CompactStorageUpgrades.RETAINING_UPGRADE.getStatisticIdentifier())), MinMaxBounds.Ints.atLeast(1)).build()))).save(consumer, "compact_storage:used_retainer_upgrade");
    }
}
