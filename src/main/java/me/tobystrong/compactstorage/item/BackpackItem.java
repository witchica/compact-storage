package me.tobystrong.compactstorage.item;

import me.tobystrong.compactstorage.CompactStorage;
import net.minecraft.item.Item;

public class BackpackItem extends Item {
    public BackpackItem() {
        super(new Properties().group(CompactStorage.compactStorageItemGroup).maxStackSize(1));
    }
}
