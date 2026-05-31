package com.witchica.compactstorage.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.witchica.compactstorage.api.inventory.ResizableContainer;

public record ResizableInventoryComponent(int inventoryWidth, int inventoryHeight) {
    public static final Codec<ResizableInventoryComponent> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(
                Codec.INT.fieldOf("inventoryWidth").forGetter(ResizableInventoryComponent::inventoryWidth),
                Codec.INT.fieldOf("inventoryHeight").forGetter(ResizableInventoryComponent::inventoryHeight))
        .apply(builder, ResizableInventoryComponent::new);
    });

    public void apply(ResizableContainer container) {
        container.setSize(this.inventoryWidth, this.inventoryHeight);
    }

    public ResizableInventoryComponent increaseWidth() {
        return new ResizableInventoryComponent(inventoryWidth + 1, inventoryHeight);
    }

    public ResizableInventoryComponent increaseHeight() {
        return new ResizableInventoryComponent(inventoryWidth, inventoryHeight + 1);
    }
}
