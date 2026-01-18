package util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class StorageTypes {
    private static final Map<String, StorageTypes> VALUES = new Object2ObjectArrayMap();

    private static final Map<DyeColor, StorageTypes> DYE_TO_TYPE_MAP = new HashMap<DyeColor, StorageTypes>();
    private static final Map<WoodType, StorageTypes> WOOD_TO_TYPE_MAP = new HashMap<WoodType, StorageTypes>();

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

    private boolean canDye = false;
    @Nullable
    private DyeColor dyeColor;
    @Nullable
    private WoodType woodType;

    public StorageTypes name(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public StorageTypes chestOpen(SoundEvent chestOpen) {
        this.chestOpenSound = chestOpen;
        return this;
    }

    public StorageTypes chestClose(SoundEvent chestClose) {
        this.chestCloseSound = chestClose;
        return this;
    }

    public StorageTypes barrelOpen(SoundEvent barrelOpen) {
        this.barrelOpenSound = barrelOpen;
        return this;
    }

    public StorageTypes barrelClose(SoundEvent barrelClose) {
        this.barrelCloseSound = barrelClose;
        return this;
    }

    public StorageTypes soundType(SoundType soundType) {
        this.soundType = soundType;
        return this;
    }

    public StorageTypes backpackOpen(SoundEvent backpackOpen) {
        this.backpackOpenSound = backpackOpen;
        return this;
    }

    public StorageTypes backpackClose(SoundEvent backpackClose) {
        this.backpackCloseSound = backpackClose;
        return this;
    }

    public StorageTypes setMetal() {
        return this;
    }

    public StorageTypes setWooden(WoodType woodType) {
        this.isWooden = true;
        this.woodType = woodType;
        StorageTypes.WOOD_TO_TYPE_MAP.put(woodType, this);

        this.soundType = SoundType.WOOD;
        this.backpackOpenSound = SoundEvents.WOODEN_TRAPDOOR_OPEN;
        this.backpackCloseSound = SoundEvents.WOODEN_TRAPDOOR_CLOSE;

        return this;
    }

    public StorageTypes setNetherWood() {
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

    @Nullable
    public DyeColor getDyeColor() {
        return dyeColor;
    }

    public StorageTypes setDyeable(DyeColor dyeColor) {
        this.canDye = true;
        this.dyeColor = dyeColor;
        StorageTypes.DYE_TO_TYPE_MAP.put(dyeColor, this);
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

    public BlockBehaviour.Properties chestProeprtiesFactory(BlockBehaviour.Properties baseBlockProperties) {
        float resistance = isWooden ? Blocks.CHEST.getExplosionResistance() : Blocks.IRON_BLOCK.getExplosionResistance();
        float destroyTime = isWooden ? Blocks.CHEST.defaultDestroyTime() : Blocks.IRON_BLOCK.defaultDestroyTime();

        if(canDye) {
            baseBlockProperties = baseBlockProperties.mapColor(getDyeColor());
        } else
            baseBlockProperties = baseBlockProperties.mapColor(MapColor.WOOD);


        return baseBlockProperties.explosionResistance(resistance).destroyTime(destroyTime).sound(getSoundType()).pushReaction(PushReaction.IGNORE).ignitedByLava().noOcclusion();
    }

    private static StorageTypes register(StorageTypes storageType) {
        VALUES.put(storageType.name, storageType);
        return storageType;
    }

    public static Stream<StorageTypes> stream() {
        return VALUES.values().stream();
    }

    public static Set<StorageTypes> values() {
        return new HashSet<>(VALUES.values());
    }

    public static Codec<StorageTypes> CODEC = Codec.stringResolver(StorageTypes::getName, VALUES::get);

    public static StorageTypes WHITE;
    public static StorageTypes ORANGE;
    public static StorageTypes MAGENTA;
    public static StorageTypes LIGHT_BLUE;
    public static StorageTypes YELLOW;
    public static StorageTypes LIME;
    public static StorageTypes PINK;
    public static StorageTypes GRAY;
    public static StorageTypes LIGHT_GRAY;
    public static StorageTypes CYAN;
    public static StorageTypes PURPLE;
    public static StorageTypes BLUE;
    public static StorageTypes BROWN;
    public static StorageTypes GREEN;
    public static StorageTypes RED;
    public static StorageTypes BLACK;
    public static StorageTypes OAK;
    public static StorageTypes SPRUCE;
    public static StorageTypes BIRCH;
    public static StorageTypes ACACIA;
    public static StorageTypes CHERRY;
    public static StorageTypes JUNGLE;
    public static StorageTypes DARK_OAK;
    public static StorageTypes PALE_OAK;
    public static StorageTypes CRIMSON;
    public static StorageTypes WARPED;
    public static StorageTypes MANGROVE;
    public static StorageTypes BAMBOO;

    static {
        WHITE = register(new StorageTypes().name("white").setMetal().setDyeable(DyeColor.WHITE));
        ORANGE = register(new StorageTypes().name("orange").setMetal().setDyeable(DyeColor.ORANGE));
        MAGENTA = register(new StorageTypes().name("magenta").setDyeable(DyeColor.MAGENTA));
        LIGHT_BLUE = register(new StorageTypes().name("light_blue").setDyeable(DyeColor.LIGHT_BLUE));
        YELLOW = register(new StorageTypes().name("yellow").setDyeable(DyeColor.YELLOW));
        LIME = register(new StorageTypes().name("lime").setDyeable(DyeColor.LIME));
        PINK = register(new StorageTypes().name("pink").setDyeable(DyeColor.PINK));
        GRAY = register(new StorageTypes().name("gray").setDyeable(DyeColor.GRAY));
        LIGHT_GRAY = register(new StorageTypes().name("light_gray").setDyeable(DyeColor.LIGHT_GRAY));
        CYAN = register(new StorageTypes().name("cyan").setDyeable(DyeColor.CYAN));
        PURPLE = register(new StorageTypes().name("purple").setDyeable(DyeColor.PURPLE));
        BLUE = register(new StorageTypes().name("blue").setDyeable(DyeColor.BLUE));
        BROWN = register(new StorageTypes().name("brown").setDyeable(DyeColor.BROWN));
        GREEN = register(new StorageTypes().name("green").setDyeable(DyeColor.GREEN));
        RED = register(new StorageTypes().name("red").setDyeable(DyeColor.RED));
        BLACK = register(new StorageTypes().name("black").setDyeable(DyeColor.BLACK));

        OAK = register(new StorageTypes().setWooden(WoodType.OAK).name("oak"));
        SPRUCE = register(new StorageTypes().setWooden(WoodType.SPRUCE).name("spruce"));
        BIRCH = register(new StorageTypes().setWooden(WoodType.BIRCH).name("birch"));
        ACACIA = register(new StorageTypes().setWooden(WoodType.ACACIA).name("acacia"));
        CHERRY = register(new StorageTypes().setWooden(WoodType.CHERRY).name("cherry").soundType(SoundType.CHERRY_WOOD).backpackOpen(SoundEvents.CHERRY_WOOD_TRAPDOOR_OPEN).backpackClose(SoundEvents.CHERRY_WOOD_TRAPDOOR_CLOSE));
        JUNGLE = register(new StorageTypes().setWooden(WoodType.JUNGLE).name("jungle"));
        DARK_OAK = register(new StorageTypes().setWooden(WoodType.DARK_OAK).name("dark_oak"));
        PALE_OAK = register(new StorageTypes().setWooden(WoodType.PALE_OAK).name("pale_oak"));
        CRIMSON = register(new StorageTypes().setWooden(WoodType.CRIMSON).name("crimson").setNetherWood());
        WARPED = register(new StorageTypes().setWooden(WoodType.WARPED).name("warped").setNetherWood());
        MANGROVE = register(new StorageTypes().setWooden(WoodType.MANGROVE).name("mangrove"));
        BAMBOO = register(new StorageTypes().setWooden(WoodType.BAMBOO).name("bamboo")
                .soundType(SoundType.BAMBOO_WOOD).backpackOpen(SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN).backpackClose(SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE));
    }
}
