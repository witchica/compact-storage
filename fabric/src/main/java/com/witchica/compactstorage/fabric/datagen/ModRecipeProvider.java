package com.witchica.compactstorage.fabric.datagen;

import com.witchica.compactstorage.data.StorageType;
import com.witchica.compactstorage.mod.CompactStorageBlocks;
import com.witchica.compactstorage.mod.CompactStorageItemTags;
import com.witchica.compactstorage.mod.CompactStorageItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import com.witchica.compactstorage.CompactStorage;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {

                shaped(RecipeCategory.MISC, CompactStorageItems.UPGRADE_WIDTH)
                        .pattern("III").pattern("BIB").pattern("III")
                        .define('I', Items.IRON_NUGGET).define('B', Items.IRON_INGOT)
                        .unlockedBy("has_compact_storage_block", has(CompactStorageItemTags.STORAGE_BLOCKS))
                        .save(output);

                shaped(RecipeCategory.MISC, CompactStorageItems.UPGRADE_HEIGHT)
                        .pattern("IBI").pattern("III").pattern("IBI")
                        .define('I', Items.IRON_NUGGET).define('B', Items.IRON_INGOT)
                        .unlockedBy("has_compact_storage_block", has(CompactStorageItemTags.STORAGE_BLOCKS))
                        .save(output);

                shaped(RecipeCategory.MISC, CompactStorageItems.UPGRADE_RETAINING)
                        .pattern("IBI").pattern("IDI").pattern("IBI").define('I', Items.IRON_NUGGET)
                        .define('B', Items.IRON_INGOT).define('D', Items.DIAMOND)
                        .unlockedBy("has_compact_storage_block", has(CompactStorageItemTags.STORAGE_BLOCKS))
                        .save(output);

                for(StorageType storageType : StorageType.values()) {
                    if(storageType.isWooden()) {
                        // Wooden Chest Recipe
                        shaped(RecipeCategory.DECORATIONS, CompactStorageBlocks.compactChests.get(storageType).asBlock())
                                .pattern("IWI")
                                .pattern("WCW")
                                .pattern("IWI")
                                .define('I', Items.IRON_INGOT)
                                .define('W', storageType.getRecipeData().planks().get())
                                .define('C', Blocks.CHEST)
                                .unlockedBy(getHasName(Items.CHEST), has(Items.CHEST))
                                .group("compact_chests")
                                .save(output);

                        // Wooden Barrel Recipe
                        shaped(RecipeCategory.DECORATIONS, CompactStorageBlocks.compactBarrels.get(storageType).asBlock())
                                .pattern("SIS")
                                .pattern("W W")
                                .pattern("SIS")
                                .define('I', Items.IRON_INGOT)
                                .define('W', storageType.getRecipeData().planks().get())
                                .define('S', storageType.getRecipeData().slab().get())
                                .unlockedBy(getHasName(Items.BARREL), has(Items.BARREL))
                                .group("compact_barrels")
                                .save(output);

                        // Wooden Drum Recipe
                        shaped(RecipeCategory.DECORATIONS, CompactStorageBlocks.itemDrums.get(storageType).asBlock())
                                .pattern("PLP")
                                .pattern("PCP")
                                .pattern("PLP")
                                .define('C', Blocks.CHEST)
                                .define('P', storageType.getRecipeData().planks().get())
                                .define('L', storageType.getRecipeData().log().get())
                                .unlockedBy(getHasName(Items.CHEST), has(Items.CHEST))
                                .group("item_drums")
                                .save(output);

                        // Wooden Pack Frame Recipe
                        shaped(RecipeCategory.TOOLS, CompactStorageItems.BACKPACK_ITEMS.get(storageType).asItem())
                                .pattern("TST")
                                .pattern("TCT")
                                .pattern("TLT")
                                .define('C', Blocks.CHEST)
                                .define('T', Items.STICK)
                                .define('S', Items.STRING)
                                .define('L', storageType.getRecipeData().log().get())
                                .unlockedBy(getHasName(Items.CHEST), has(Items.CHEST))
                                .group("pack_frames")
                                .save(output);
                    } else {
                        // Colorful Chest Recipe
                        shaped(RecipeCategory.DECORATIONS, CompactStorageBlocks.compactChests.get(storageType).asBlock())
                                .pattern("IWI")
                                .pattern("ICI")
                                .pattern("III")
                                .define('I', Items.IRON_INGOT)
                                .define('W', storageType.getRecipeData().dye().get())
                                .define('C', Blocks.CHEST)
                                .unlockedBy(getHasName(Items.CHEST), has(Items.CHEST))
                                .group("compact_chests")
                                .save(output);

                        // Colorful Barrel Recipe
                        shaped(RecipeCategory.DECORATIONS, CompactStorageBlocks.compactBarrels.get(storageType).asBlock())
                                .pattern("WSW")
                                .pattern("IDI")
                                .pattern("WSW")
                                .define('I', Items.IRON_INGOT)
                                .define('W', ItemTags.PLANKS)
                                .define('S', ItemTags.SLABS)
                                .define('D', storageType.getRecipeData().dye().get())
                                .unlockedBy(getHasName(Items.BARREL), has(Items.BARREL))
                                .group("compact_barrels")
                                .save(output);

                        // Colorful Drum Recipe
                        shaped(RecipeCategory.DECORATIONS, CompactStorageBlocks.itemDrums.get(storageType).asBlock())
                                .pattern("PDP")
                                .pattern("PCP")
                                .pattern("PLP")
                                .define('C', Blocks.CHEST)
                                .define('P', ItemTags.PLANKS)
                                .define('L', ItemTags.LOGS)
                                .define('D', storageType.getRecipeData().dye().get())
                                .unlockedBy(getHasName(Items.CHEST), has(Items.CHEST))
                                .group("item_drums")
                                .save(output);

                        // Wooden Pack Frame Recipe
                        shaped(RecipeCategory.TOOLS, CompactStorageItems.BACKPACK_ITEMS.get(storageType).asItem())
                                .pattern("IWI")
                                .pattern("ICI")
                                .pattern("IWI")
                                .define('C', Blocks.CHEST)
                                .define('W', storageType.getRecipeData().wool().get())
                                .define('I', Items.IRON_INGOT)
                                .unlockedBy(getHasName(Items.CHEST), has(Items.CHEST))
                                .group("backpacks")
                                .save(output);
                    }
                }
            }
        };
    }

    @Override
    public String getName() {
        return CompactStorage.MOD_ID;
    }
}
