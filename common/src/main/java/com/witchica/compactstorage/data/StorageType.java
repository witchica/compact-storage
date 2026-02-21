package com.witchica.compactstorage.data;

import com.mojang.serialization.Codec;
import com.witchica.compactstorage.CompactStorage;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
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

import java.util.*;
import java.util.stream.Stream;

public class StorageType {
    public record RecipeData(Optional<ItemLike> planks, Optional<TagKey<Block>> log, Optional<ItemLike> slab, Optional<ItemLike> wool, Optional<ItemLike> dye, Block particle) {
        public RecipeData(ItemLike wool, ItemLike dye, Block particle) {
            this(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(wool), Optional.of(dye), particle);
        }
        public RecipeData(ItemLike planks, TagKey<Block> log, Block slab) {
            this(Optional.of(planks), Optional.of(log), Optional.of(slab), Optional.empty(), Optional.empty(), (Block) planks);
        }
    }

    private static final Map<String, StorageType> VALUES = new Object2ObjectArrayMap();

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
        return isWooden ? getName() + "_compact_chest" : "compact_chest_" + getName();
    }

    public String barrelNameFactory() {
        return isWooden ? getName() + "_compact_barrel" : "compact_barrel_" + getName();
    }

    public String itemDrumNameFactory() {
        return getName() + "_drum";
    }

    public BlockBehaviour.Properties chestProeprtiesFactory(BlockBehaviour.Properties baseBlockProperties) {
        float resistance = isWooden ? Blocks.CHEST.getExplosionResistance() : Blocks.IRON_BLOCK.getExplosionResistance();
        float destroyTime = isWooden ? Blocks.CHEST.defaultDestroyTime() : Blocks.IRON_BLOCK.defaultDestroyTime();

        if(canDye) {
            baseBlockProperties = baseBlockProperties.mapColor(getDyeColor());
        } else
            baseBlockProperties = baseBlockProperties.mapColor(MapColor.WOOD);


        return baseBlockProperties.explosionResistance(resistance).destroyTime(destroyTime).sound(getSoundType()).pushReaction(PushReaction.IGNORE).ignitedByLava().noOcclusion();
    }

    public BlockBehaviour.Properties barrelPropertiesFactory(BlockBehaviour.Properties baseBlockProperties) {
        float resistance = isWooden ? Blocks.BARREL.getExplosionResistance() : Blocks.IRON_BLOCK.getExplosionResistance();
        float destroyTime = isWooden ? Blocks.BARREL.defaultDestroyTime() : Blocks.IRON_BLOCK.defaultDestroyTime();

        if(canDye) {
            baseBlockProperties = baseBlockProperties.mapColor(getDyeColor());
        } else
            baseBlockProperties = baseBlockProperties.mapColor(MapColor.WOOD);


        return baseBlockProperties.explosionResistance(resistance).destroyTime(destroyTime).sound(getSoundType()).pushReaction(PushReaction.IGNORE).ignitedByLava().noOcclusion();
    }

    private static StorageType register(StorageType storageType) {
        VALUES.put(storageType.name, storageType);
        return storageType;
    }

    public static Stream<StorageType> stream() {
        return VALUES.values().stream();
    }

    public static Set<StorageType> values() {
        return new HashSet<>(VALUES.values());
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
        WHITE = register(new StorageType().name("white").recipe(new RecipeData(Blocks.WHITE_WOOL, Items.WHITE_DYE, Blocks.WHITE_CONCRETE)).setMetal().setDyeable(DyeColor.WHITE)).inventoryBackground(2, 3).setUiTitleColor(0x1b1b1e);
        ORANGE = register(new StorageType().name("orange").recipe(new RecipeData(Blocks.ORANGE_WOOL, Items.ORANGE_DYE, Blocks.ORANGE_CONCRETE)).setMetal().setDyeable(DyeColor.ORANGE)).inventoryBackground(6, 2).setUiTitleColor(0x8e2818);
        MAGENTA = register(new StorageType().name("magenta").recipe(new RecipeData(Blocks.MAGENTA_WOOL, Items.MAGENTA_DYE, Blocks.MAGENTA_CONCRETE)).setDyeable(DyeColor.MAGENTA)).inventoryBackground(5, 2).setUiTitleColor(0x64185c);
        LIGHT_BLUE = register(new StorageType().name("light_blue").recipe(new RecipeData(Blocks.LIGHT_BLUE_WOOL, Items.LIGHT_BLUE_DYE, Blocks.LIGHT_BLUE_CONCRETE)).setDyeable(DyeColor.LIGHT_BLUE)).inventoryBackground(2, 2).setUiTitleColor(0x185374);
        YELLOW = register(new StorageType().name("yellow").recipe(new RecipeData(Blocks.YELLOW_WOOL, Items.YELLOW_DYE, Blocks.YELLOW_CONCRETE)).setDyeable(DyeColor.YELLOW)).inventoryBackground(3, 3).setUiTitleColor(0x927218);
        LIME = register(new StorageType().name("lime").recipe(new RecipeData(Blocks.LIME_WOOL, Items.LIME_DYE, Blocks.LIME_CONCRETE)).setDyeable(DyeColor.LIME)).inventoryBackground(4, 2).setUiTitleColor(0x286418);
        PINK = register(new StorageType().name("pink").recipe(new RecipeData(Blocks.PINK_WOOL, Items.PINK_DYE, Blocks.PINK_CONCRETE)).setDyeable(DyeColor.PINK)).inventoryBackground(7, 2).setUiTitleColor(0x89324c);
        GRAY = register(new StorageType().name("gray").recipe(new RecipeData(Blocks.GRAY_WOOL, Items.GRAY_DYE, Blocks.GRAY_CONCRETE)).setDyeable(DyeColor.GRAY)).inventoryBackground(0, 2).setUiTitleColor(0x181818);
        LIGHT_GRAY = register(new StorageType().name("light_gray").recipe(new RecipeData(Blocks.LIGHT_GRAY_WOOL, Items.LIGHT_GRAY_DYE, Blocks.LIGHT_GRAY_CONCRETE)).setDyeable(DyeColor.LIGHT_GRAY)).inventoryBackground(3, 2).setUiTitleColor(0x41413c);
        CYAN = register(new StorageType().name("cyan").recipe(new RecipeData(Blocks.CYAN_WOOL, Items.CYAN_DYE, Blocks.CYAN_CONCRETE)).setDyeable(DyeColor.CYAN)).inventoryBackground(7, 1).setUiTitleColor(0x184040);
        PURPLE = register(new StorageType().name("purple").recipe(new RecipeData(Blocks.PURPLE_WOOL, Items.PURPLE_DYE, Blocks.PURPLE_CONCRETE)).setDyeable(DyeColor.PURPLE)).inventoryBackground(0, 3).setUiTitleColor(0x301857);
        BLUE = register(new StorageType().name("blue").recipe(new RecipeData(Blocks.BLUE_WOOL, Items.BLUE_DYE, Blocks.BLUE_CONCRETE)).setDyeable(DyeColor.BLUE)).inventoryBackground(5, 1).setUiTitleColor(0x151541);
        BROWN = register(new StorageType().name("brown").recipe(new RecipeData(Blocks.BROWN_WOOL, Items.BROWN_DYE, Blocks.BROWN_CONCRETE)).setDyeable(DyeColor.BROWN)).inventoryBackground(6, 1).setUiTitleColor(0x170000);
        GREEN = register(new StorageType().name("green").recipe(new RecipeData(Blocks.GREEN_WOOL, Items.GREEN_DYE, Blocks.GREEN_CONCRETE)).setDyeable(DyeColor.GREEN)).inventoryBackground(1, 2).setUiTitleColor(0x182518);
        RED = register(new StorageType().name("red").recipe(new RecipeData(Blocks.RED_WOOL, Items.RED_DYE, Blocks.RED_CONCRETE)).setDyeable(DyeColor.RED)).inventoryBackground(1, 3).setUiTitleColor(0x511818);
        BLACK = register(new StorageType().name("black").recipe(new RecipeData(Blocks.BLACK_WOOL, Items.BLACK_DYE, Blocks.BLACK_CONCRETE)).setDyeable(DyeColor.BLACK)).inventoryBackground(4, 1).setUiTitleColor(0xfaefee);

        OAK = register(new StorageType().setWooden(WoodType.OAK).name("oak").recipe(new RecipeData(Blocks.OAK_PLANKS, BlockTags.OAK_LOGS, Blocks.OAK_SLAB))).inventoryBackground(0, 1).setUiTitleColor(0x4c3d26);
        SPRUCE = register(new StorageType().setWooden(WoodType.SPRUCE).name("spruce").recipe(new RecipeData(Blocks.SPRUCE_PLANKS, BlockTags.SPRUCE_LOGS, Blocks.SPRUCE_SLAB))).inventoryBackground(1, 1).setUiTitleColor(0x443321);
        BIRCH = register(new StorageType().setWooden(WoodType.BIRCH).name("birch").recipe(new RecipeData(Blocks.BIRCH_PLANKS, BlockTags.BIRCH_LOGS, Blocks.BIRCH_SLAB))).inventoryBackground(2, 0).setUiTitleColor(0x514f47);
        ACACIA = register(new StorageType().setWooden(WoodType.ACACIA).name("acacia").recipe(new RecipeData(Blocks.ACACIA_PLANKS, BlockTags.ACACIA_LOGS, Blocks.ACACIA_SLAB))).inventoryBackground(1, 0).setUiTitleColor(0x4b473e);
        CHERRY = register(new StorageType().setWooden(WoodType.CHERRY).name("cherry").recipe(new RecipeData(Blocks.CHERRY_PLANKS, BlockTags.CHERRY_LOGS, Blocks.CHERRY_SLAB)).soundType(SoundType.CHERRY_WOOD).backpackOpen(SoundEvents.CHERRY_WOOD_TRAPDOOR_OPEN).backpackClose(SoundEvents.CHERRY_WOOD_TRAPDOOR_CLOSE)).inventoryBackground(3, 0).setUiTitleColor(0x271620);
        JUNGLE = register(new StorageType().setWooden(WoodType.JUNGLE).name("jungle").recipe(new RecipeData(Blocks.JUNGLE_PLANKS, BlockTags.JUNGLE_LOGS, Blocks.JUNGLE_SLAB))).inventoryBackground(6, 0).setUiTitleColor(0x3e3013);
        DARK_OAK = register(new StorageType().setWooden(WoodType.DARK_OAK).name("dark_oak").recipe(new RecipeData(Blocks.DARK_OAK_PLANKS, BlockTags.DARK_OAK_LOGS, Blocks.DARK_OAK_SLAB))).inventoryBackground(5, 0).setUiTitleColor(0x292011);
        PALE_OAK = register(new StorageType().setWooden(WoodType.PALE_OAK).name("pale_oak").recipe(new RecipeData(Blocks.PALE_OAK_PLANKS, BlockTags.PALE_OAK_LOGS, Blocks.PALE_OAK_SLAB))).inventoryBackground(4, 3).setUiTitleColor(0x4e4340);
        CRIMSON = register(new StorageType().setWooden(WoodType.CRIMSON).name("crimson").recipe(new RecipeData(Blocks.CRIMSON_PLANKS, BlockTags.CRIMSON_STEMS, Blocks.CRIMSON_SLAB)).setNetherWood()).inventoryBackground(4, 0).setUiTitleColor(0x3f1e2d);
        WARPED = register(new StorageType().setWooden(WoodType.WARPED).name("warped").recipe(new RecipeData(Blocks.WARPED_PLANKS, BlockTags.WARPED_STEMS, Blocks.WARPED_PLANKS)).setNetherWood()).inventoryBackground(2, 1).setUiTitleColor(0x452d5c);
        MANGROVE = register(new StorageType().setWooden(WoodType.MANGROVE).name("mangrove").recipe(new RecipeData(Blocks.MANGROVE_PLANKS, BlockTags.MANGROVE_LOGS, Blocks.MANGROVE_SLAB))).inventoryBackground(7, 0).setUiTitleColor(0x3c2f23);
        BAMBOO = register(new StorageType().setWooden(WoodType.BAMBOO).name("bamboo").recipe(new RecipeData(Blocks.BAMBOO_PLANKS, BlockTags.BAMBOO_BLOCKS, Blocks.BAMBOO_SLAB))
                .soundType(SoundType.BAMBOO_WOOD).backpackOpen(SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN).backpackClose(SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE)).inventoryBackground(3, 1).setUiTitleColor(0x907e3a);
    }
}
