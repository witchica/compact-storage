package com.witchica.compactstorage.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.witchica.compactstorage.block.base.BaseItemDrumBlock;
import com.witchica.compactstorage.block.entity.base.BaseItemDrumBlockEntity;
import com.witchica.compactstorage.data.StorageType;
import net.minecraft.world.level.block.BaseEntityBlock;

public class ItemDrumBlock extends BaseItemDrumBlock {
    public static final MapCodec<ItemDrumBlock> CODEC = RecordCodecBuilder.mapCodec(itemDrumBlockInstance -> itemDrumBlockInstance.group(StorageType.CODEC.fieldOf("storageType").forGetter(ItemDrumBlock::getStorageType), propertiesCodec()).apply(itemDrumBlockInstance, ItemDrumBlock::new));

    public ItemDrumBlock(StorageType storageType, Properties properties) {
        super(storageType, properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
