package com.witchica.compactstorage.data;

import com.mojang.serialization.Codec;
import com.sun.source.tree.Tree;
import com.witchica.compactstorage.CompactStorage;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.stream.Stream;

public class StorageType implements Comparable<StorageType> {
    public record RecipeData(Optional<ItemLike> planks, Optional<TagKey<Item>> log, Optional<ItemLike> slab, Optional<ItemLike> wool, Optional<ItemLike> dye, Block particle) {
        public RecipeData(ItemLike wool, ItemLike dye, Block particle) {
            this(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(wool), Optional.of(dye), particle);
        }
        public RecipeData(ItemLike planks, TagKey<Item> log, Block slab) {
            this(Optional.of(planks), Optional.of(log), Optional.of(slab), Optional.empty(), Optional.empty(), (Block) planks);
        }
    }

    public record BackpackModelInfo(Block planks, Block log, Block wool, Block straps) {
        public BackpackModelInfo(Block wool, Block straps) {
            this(null, null, wool, straps);
        }
        public BackpackModelInfo(Block planks, Block log, Block wool) {
            this(planks, log, wool, null);
        }
    }

    private static final Map<String, StorageType> VALUES = new TreeMap<>();

    private static final Map<DyeColor, StorageType> DYE_TO_TYPE_MAP = new HashMap<DyeColor, StorageType>();
    private static final Map<WoodType, StorageType> WOOD_TO_TYPE_MAP = new HashMap<WoodType, StorageType>();

    public static StorageType fromDye(DyeColor dyeColor) {
        return DYE_TO_TYPE_MAP.get(dyeColor);
    }

    public static StorageType fromWood(WoodType woodType) {
        return WOOD_TO_TYPE_MAP.get(woodType);
    }

    public SoundEvent chestOpenSound = SoundEvents.CHEST_OPEN;
    public SoundEvent chestCloseSound = SoundEvents.CHEST_CLOSE;
    public SoundEvent barrelOpenSound = SoundEvents.BARREL_OPEN;
    public SoundEvent barrelCloseSound = SoundEvents.BARREL_CLOSE;
    public SoundType soundType = SoundType.IRON;
    public SoundEvent backpackOpenSound = SoundEvents.WOOL_PLACE;
    public SoundEvent backpackCloseSound = SoundEvents.WOOL_BREAK;

    private boolean isWooden = false;
    private boolean isNetherWood = false;
    private String name = "";
    private RecipeData recipeData;

    private Identifier slotsTexture;

    private boolean canDye = false;
    @Nullable
    private DyeColor dyeColor;
    @Nullable
    private WoodType woodType;
    private Vector2i uiCoords;
    private int uiTitleColor;
    private BackpackModelInfo backpackModelInfo;

    public StorageType name(String name) {
        this.name = name;
        this.slotsTexture = Identifier.fromNamespaceAndPath(CompactStorage.MOD_ID, "textures/gui/slots/" + name + ".png");
        return this;
    }

    public Identifier getSlotsTexture() {
        return slotsTexture;
    }

    public StorageType recipe(RecipeData recipeData) {
        this.recipeData = recipeData;
        return this;
    }

    public RecipeData getRecipeData() {
        return recipeData;
    }

    public String getName() {
        return name;
    }

    public StorageType chestOpen(SoundEvent chestOpen) {
        this.chestOpenSound = chestOpen;
        return this;
    }

    public StorageType chestClose(SoundEvent chestClose) {
        this.chestCloseSound = chestClose;
        return this;
    }

    public StorageType barrelOpen(SoundEvent barrelOpen) {
        this.barrelOpenSound = barrelOpen;
        return this;
    }

    public StorageType barrelClose(SoundEvent barrelClose) {
        this.barrelCloseSound = barrelClose;
        return this;
    }

    public StorageType soundType(SoundType soundType) {
        this.soundType = soundType;
        return this;
    }

    public StorageType backpackOpen(SoundEvent backpackOpen) {
        this.backpackOpenSound = backpackOpen;
        return this;
    }

    public StorageType backpackClose(SoundEvent backpackClose) {
        this.backpackCloseSound = backpackClose;
        return this;
    }

    public StorageType setMetal() {
        return this;
    }

    public StorageType inventoryBackground(int x, int y) {
        this.uiCoords = new Vector2i(x, y);
        return this;
    }

    public Vector2i getInventoryCoords() {
        return this.uiCoords;
    }

    public StorageType setWooden(WoodType woodType) {
        this.isWooden = true;
        this.woodType = woodType;
        StorageType.WOOD_TO_TYPE_MAP.put(woodType, this);

        this.soundType = SoundType.WOOD;
        this.backpackOpenSound = SoundEvents.WOODEN_TRAPDOOR_OPEN;
        this.backpackCloseSound = SoundEvents.WOODEN_TRAPDOOR_CLOSE;

        return this;
    }

    public StorageType setNetherWood() {
        this.isNetherWood = true;

        this.soundType = SoundType.NETHER_WOOD;
        this.backpackOpenSound = SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN;
        this.backpackCloseSound = SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE;

        return this;
    }

    public boolean isNetherWood() {
        return this.isNetherWood;
    }

    public boolean isWooden() {
        return isWooden;
    }

    public boolean canDye() {
        return canDye;
    }

    public StorageType setUiTitleColor(int color) {
        this.uiTitleColor = color;
        return this;
    }

    public int getUiTitleColor() {
        return uiTitleColor;
    }

    @Nullable
    public DyeColor getDyeColor() {
        return dyeColor;
    }

    public StorageType setDyeable(DyeColor dyeColor) {
        this.canDye = true;
        this.dyeColor = dyeColor;
        StorageType.DYE_TO_TYPE_MAP.put(dyeColor, this);
        return this;
    }

    public SoundType getSoundType() {
        return this.soundType;
    }

    @Nullable
    public WoodType getWoodType() {
        return woodType;
    }

    @Override
    public String toString() {
        return name;
    }

    public String chestNameFactory() {
        return getName() + "_compact_chest";
    }

    public String barrelNameFactory() {
        return getName() + "_compact_barrel";
    }

    public String itemDrumNameFactory() {
        return getName() + "_drum";
    }

    public String backpackNameFactory() {
        return getName() + (isWooden() ? "_pack_frame" : "_backpack");
    }


    public BlockBehaviour.Properties chestProeprtiesFactory(BlockBehaviour.Properties baseBlockProperties) {
        float resistance = isWooden ? Blocks.CHEST.getExplosionResistance() : Blocks.IRON_BLOCK.getExplosionResistance();
        float destroyTime = isWooden ? Blocks.CHEST.defaultDestroyTime() : Blocks.IRON_BLOCK.defaultDestroyTime();

        if(canDye) {
            baseBlockProperties = baseBlockProperties.mapColor(getDyeColor());
        } else
            baseBlockProperties = baseBlockProperties.mapColor(MapColor.WOOD);

        if(isWooden()) {
            baseBlockProperties = baseBlockProperties.ignitedByLava();
        }


        return baseBlockProperties.explosionResistance(resistance).destroyTime(destroyTime).sound(getSoundType()).pushReaction(PushReaction.IGNORE).noOcclusion();
    }

    public BlockBehaviour.Properties barrelPropertiesFactory(BlockBehaviour.Properties baseBlockProperties) {
        float resistance = isWooden ? Blocks.BARREL.getExplosionResistance() : Blocks.IRON_BLOCK.getExplosionResistance();
        float destroyTime = isWooden ? Blocks.BARREL.defaultDestroyTime() : Blocks.IRON_BLOCK.defaultDestroyTime();

        if(canDye) {
            baseBlockProperties = baseBlockProperties.mapColor(getDyeColor());
        } else
            baseBlockProperties = baseBlockProperties.mapColor(MapColor.WOOD);

        if(isWooden()) {
            baseBlockProperties = baseBlockProperties.ignitedByLava();
        }

        return baseBlockProperties.explosionResistance(resistance).destroyTime(destroyTime).sound(getSoundType()).pushReaction(PushReaction.IGNORE).noOcclusion();
    }

    public StorageType setBackpackModelInfo(BackpackModelInfo backpackModelInfo) {
        this.backpackModelInfo = backpackModelInfo;
        return this;
    }

    public BackpackModelInfo backpackModelInfo() {
        return backpackModelInfo;
    }

    private static StorageType register(StorageType storageType) {
        VALUES.put(storageType.name, storageType);
        return storageType;
    }

    public static Stream<StorageType> stream() {
        return VALUES.values().stream();
    }

    public static Set<StorageType> values() {
        return new TreeSet<>(VALUES.values());
    }

    public static Codec<StorageType> CODEC = Codec.stringResolver(StorageType::getName, VALUES::get);

    public static StorageType WHITE;
    public static StorageType ORANGE;
    public static StorageType MAGENTA;
    public static StorageType LIGHT_BLUE;
    public static StorageType YELLOW;
    public static StorageType LIME;
    public static StorageType PINK;
    public static StorageType GRAY;
    public static StorageType LIGHT_GRAY;
    public static StorageType CYAN;
    public static StorageType PURPLE;
    public static StorageType BLUE;
    public static StorageType BROWN;
    public static StorageType GREEN;
    public static StorageType RED;
    public static StorageType BLACK;
    public static StorageType OAK;
    public static StorageType SPRUCE;
    public static StorageType BIRCH;
    public static StorageType ACACIA;
    public static StorageType CHERRY;
    public static StorageType JUNGLE;
    public static StorageType DARK_OAK;
    public static StorageType PALE_OAK;
    public static StorageType CRIMSON;
    public static StorageType WARPED;
    public static StorageType MANGROVE;
    public static StorageType BAMBOO;

    static {
        WHITE = register(new StorageType().name("white").recipe(new RecipeData(Blocks.WHITE_WOOL, Items.WHITE_DYE, Blocks.WHITE_CONCRETE)).setMetal().setDyeable(DyeColor.WHITE)).inventoryBackground(2, 3).setUiTitleColor(0x1b1b1e).setBackpackModelInfo(new BackpackModelInfo(Blocks.WHITE_WOOL, Blocks.LIGHT_GRAY_WOOL));
        ORANGE = register(new StorageType().name("orange").recipe(new RecipeData(Blocks.ORANGE_WOOL, Items.ORANGE_DYE, Blocks.ORANGE_CONCRETE)).setMetal().setDyeable(DyeColor.ORANGE)).inventoryBackground(6, 2).setUiTitleColor(0x8e2818).setBackpackModelInfo(new BackpackModelInfo(Blocks.ORANGE_WOOL, Blocks.RED_WOOL));
        MAGENTA = register(new StorageType().name("magenta").recipe(new RecipeData(Blocks.MAGENTA_WOOL, Items.MAGENTA_DYE, Blocks.MAGENTA_CONCRETE)).setDyeable(DyeColor.MAGENTA)).inventoryBackground(5, 2).setUiTitleColor(0x64185c).setBackpackModelInfo(new BackpackModelInfo(Blocks.MAGENTA_WOOL, Blocks.PINK_WOOL));
        LIGHT_BLUE = register(new StorageType().name("light_blue").recipe(new RecipeData(Blocks.LIGHT_BLUE_WOOL, Items.LIGHT_BLUE_DYE, Blocks.LIGHT_BLUE_CONCRETE)).setDyeable(DyeColor.LIGHT_BLUE)).inventoryBackground(2, 2).setUiTitleColor(0x185374).setBackpackModelInfo(new BackpackModelInfo(Blocks.LIGHT_BLUE_WOOL, Blocks.BLUE_WOOL));
        YELLOW = register(new StorageType().name("yellow").recipe(new RecipeData(Blocks.YELLOW_WOOL, Items.YELLOW_DYE, Blocks.YELLOW_CONCRETE)).setDyeable(DyeColor.YELLOW)).inventoryBackground(3, 3).setUiTitleColor(0x927218).setBackpackModelInfo(new BackpackModelInfo(Blocks.YELLOW_WOOL, Blocks.WHITE_WOOL));
        LIME = register(new StorageType().name("lime").recipe(new RecipeData(Blocks.LIME_WOOL, Items.LIME_DYE, Blocks.LIME_CONCRETE)).setDyeable(DyeColor.LIME)).inventoryBackground(4, 2).setUiTitleColor(0x286418).setBackpackModelInfo(new BackpackModelInfo(Blocks.LIME_WOOL, Blocks.GREEN_WOOL));
        PINK = register(new StorageType().name("pink").recipe(new RecipeData(Blocks.PINK_WOOL, Items.PINK_DYE, Blocks.PINK_CONCRETE)).setDyeable(DyeColor.PINK)).inventoryBackground(7, 2).setUiTitleColor(0x89324c).setBackpackModelInfo(new BackpackModelInfo(Blocks.PINK_WOOL, Blocks.MAGENTA_WOOL));
        GRAY = register(new StorageType().name("gray").recipe(new RecipeData(Blocks.GRAY_WOOL, Items.GRAY_DYE, Blocks.GRAY_CONCRETE)).setDyeable(DyeColor.GRAY)).inventoryBackground(0, 2).setUiTitleColor(0x181818).setBackpackModelInfo(new BackpackModelInfo(Blocks.GRAY_WOOL, Blocks.LIGHT_GRAY_WOOL));
        LIGHT_GRAY = register(new StorageType().name("light_gray").recipe(new RecipeData(Blocks.LIGHT_GRAY_WOOL, Items.LIGHT_GRAY_DYE, Blocks.LIGHT_GRAY_CONCRETE)).setDyeable(DyeColor.LIGHT_GRAY)).inventoryBackground(3, 2).setUiTitleColor(0x41413c).setBackpackModelInfo(new BackpackModelInfo(Blocks.LIGHT_GRAY_WOOL, Blocks.GRAY_WOOL));
        CYAN = register(new StorageType().name("cyan").recipe(new RecipeData(Blocks.CYAN_WOOL, Items.CYAN_DYE, Blocks.CYAN_CONCRETE)).setDyeable(DyeColor.CYAN)).inventoryBackground(7, 1).setUiTitleColor(0x184040).setBackpackModelInfo(new BackpackModelInfo(Blocks.CYAN_WOOL, Blocks.BLUE_WOOL));
        PURPLE = register(new StorageType().name("purple").recipe(new RecipeData(Blocks.PURPLE_WOOL, Items.PURPLE_DYE, Blocks.PURPLE_CONCRETE)).setDyeable(DyeColor.PURPLE)).inventoryBackground(0, 3).setUiTitleColor(0x301857).setBackpackModelInfo(new BackpackModelInfo(Blocks.PURPLE_WOOL, Blocks.BLACK_WOOL));
        BLUE = register(new StorageType().name("blue").recipe(new RecipeData(Blocks.BLUE_WOOL, Items.BLUE_DYE, Blocks.BLUE_CONCRETE)).setDyeable(DyeColor.BLUE)).inventoryBackground(5, 1).setUiTitleColor(0x151541).setBackpackModelInfo(new BackpackModelInfo(Blocks.BLUE_WOOL, Blocks.LIGHT_BLUE_WOOL));
        BROWN = register(new StorageType().name("brown").recipe(new RecipeData(Blocks.BROWN_WOOL, Items.BROWN_DYE, Blocks.BROWN_CONCRETE)).setDyeable(DyeColor.BROWN)).inventoryBackground(6, 1).setUiTitleColor(0x170000).setBackpackModelInfo(new BackpackModelInfo(Blocks.BROWN_WOOL, Blocks.BLACK_WOOL));
        GREEN = register(new StorageType().name("green").recipe(new RecipeData(Blocks.GREEN_WOOL, Items.GREEN_DYE, Blocks.GREEN_CONCRETE)).setDyeable(DyeColor.GREEN)).inventoryBackground(1, 2).setUiTitleColor(0x182518).setBackpackModelInfo(new BackpackModelInfo(Blocks.GREEN_WOOL, Blocks.LIME_WOOL));
        RED = register(new StorageType().name("red").recipe(new RecipeData(Blocks.RED_WOOL, Items.RED_DYE, Blocks.RED_CONCRETE)).setDyeable(DyeColor.RED)).inventoryBackground(1, 3).setUiTitleColor(0x511818).setBackpackModelInfo(new BackpackModelInfo(Blocks.RED_WOOL, Blocks.ORANGE_WOOL));
        BLACK = register(new StorageType().name("black").recipe(new RecipeData(Blocks.BLACK_WOOL, Items.BLACK_DYE, Blocks.BLACK_CONCRETE)).setDyeable(DyeColor.BLACK)).inventoryBackground(4, 1).setUiTitleColor(0xfaefee).setBackpackModelInfo(new BackpackModelInfo(Blocks.BLACK_WOOL, Blocks.GRAY_WOOL));

        OAK = register(new StorageType().setWooden(WoodType.OAK).name("oak").recipe(new RecipeData(Blocks.OAK_PLANKS, ItemTags.OAK_LOGS, Blocks.OAK_SLAB))).inventoryBackground(0, 1).setUiTitleColor(0x4c3d26).setBackpackModelInfo(new BackpackModelInfo(Blocks.OAK_PLANKS, Blocks.OAK_LOG, Blocks.BROWN_WOOL));
        SPRUCE = register(new StorageType().setWooden(WoodType.SPRUCE).name("spruce").recipe(new RecipeData(Blocks.SPRUCE_PLANKS, ItemTags.SPRUCE_LOGS, Blocks.SPRUCE_SLAB))).inventoryBackground(1, 1).setUiTitleColor(0x443321).setBackpackModelInfo(new BackpackModelInfo(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_LOG, Blocks.BROWN_WOOL));
        BIRCH = register(new StorageType().setWooden(WoodType.BIRCH).name("birch").recipe(new RecipeData(Blocks.BIRCH_PLANKS, ItemTags.BIRCH_LOGS, Blocks.BIRCH_SLAB))).inventoryBackground(2, 0).setUiTitleColor(0x514f47).setBackpackModelInfo(new BackpackModelInfo(Blocks.BIRCH_PLANKS, Blocks.BIRCH_LOG, Blocks.WHITE_WOOL));
        ACACIA = register(new StorageType().setWooden(WoodType.ACACIA).name("acacia").recipe(new RecipeData(Blocks.ACACIA_PLANKS, ItemTags.ACACIA_LOGS, Blocks.ACACIA_SLAB))).inventoryBackground(1, 0).setUiTitleColor(0x4b473e).setBackpackModelInfo(new BackpackModelInfo(Blocks.ACACIA_PLANKS, Blocks.ACACIA_LOG, Blocks.RED_WOOL));
        CHERRY = register(new StorageType().setWooden(WoodType.CHERRY).name("cherry").recipe(new RecipeData(Blocks.CHERRY_PLANKS, ItemTags.CHERRY_LOGS, Blocks.CHERRY_SLAB)).soundType(SoundType.CHERRY_WOOD).backpackOpen(SoundEvents.CHERRY_WOOD_TRAPDOOR_OPEN).backpackClose(SoundEvents.CHERRY_WOOD_TRAPDOOR_CLOSE)).inventoryBackground(3, 0).setUiTitleColor(0x271620).setBackpackModelInfo(new BackpackModelInfo(Blocks.CHERRY_PLANKS, Blocks.CHERRY_LOG, Blocks.PINK_WOOL));
        JUNGLE = register(new StorageType().setWooden(WoodType.JUNGLE).name("jungle").recipe(new RecipeData(Blocks.JUNGLE_PLANKS, ItemTags.JUNGLE_LOGS, Blocks.JUNGLE_SLAB))).inventoryBackground(6, 0).setUiTitleColor(0x3e3013).setBackpackModelInfo(new BackpackModelInfo(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_LOG, Blocks.BROWN_WOOL));
        DARK_OAK = register(new StorageType().setWooden(WoodType.DARK_OAK).name("dark_oak").recipe(new RecipeData(Blocks.DARK_OAK_PLANKS, ItemTags.DARK_OAK_LOGS, Blocks.DARK_OAK_SLAB))).inventoryBackground(5, 0).setUiTitleColor(0x292011).setBackpackModelInfo(new BackpackModelInfo(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_LOG, Blocks.BLACK_WOOL));
        PALE_OAK = register(new StorageType().setWooden(WoodType.PALE_OAK).name("pale_oak").recipe(new RecipeData(Blocks.PALE_OAK_PLANKS, ItemTags.PALE_OAK_LOGS, Blocks.PALE_OAK_SLAB))).inventoryBackground(4, 3).setUiTitleColor(0x4e4340).setBackpackModelInfo(new BackpackModelInfo(Blocks.PALE_OAK_PLANKS, Blocks.PALE_OAK_LOG, Blocks.WHITE_WOOL));
        CRIMSON = register(new StorageType().setWooden(WoodType.CRIMSON).name("crimson").recipe(new RecipeData(Blocks.CRIMSON_PLANKS, ItemTags.CRIMSON_STEMS, Blocks.CRIMSON_SLAB)).setNetherWood()).inventoryBackground(4, 0).setUiTitleColor(0x3f1e2d).setBackpackModelInfo(new BackpackModelInfo(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_STEM, Blocks.RED_WOOL));
        WARPED = register(new StorageType().setWooden(WoodType.WARPED).name("warped").recipe(new RecipeData(Blocks.WARPED_PLANKS, ItemTags.WARPED_STEMS, Blocks.WARPED_PLANKS)).setNetherWood()).inventoryBackground(2, 1).setUiTitleColor(0x452d5c).setBackpackModelInfo(new BackpackModelInfo(Blocks.WARPED_PLANKS, Blocks.WARPED_STEM, Blocks.CYAN_WOOL));
        MANGROVE = register(new StorageType().setWooden(WoodType.MANGROVE).name("mangrove").recipe(new RecipeData(Blocks.MANGROVE_PLANKS, ItemTags.MANGROVE_LOGS, Blocks.MANGROVE_SLAB))).inventoryBackground(7, 0).setUiTitleColor(0x3c2f23).setBackpackModelInfo(new BackpackModelInfo(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_LOG, Blocks.WHITE_WOOL));
        BAMBOO = register(new StorageType().setWooden(WoodType.BAMBOO).name("bamboo").recipe(new RecipeData(Blocks.BAMBOO_PLANKS, ItemTags.BAMBOO_BLOCKS, Blocks.BAMBOO_SLAB))
                .soundType(SoundType.BAMBOO_WOOD).backpackOpen(SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN).backpackClose(SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE)).inventoryBackground(3, 1).setUiTitleColor(0x907e3a).setBackpackModelInfo(new BackpackModelInfo(Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_BLOCK, Blocks.WHITE_WOOL));
    }

    @Override
    public int compareTo(@NonNull StorageType o) {
        return name.compareTo(o.name);
    }
}
