package com.witchica.compactstorage.api.inventory;

public interface ResizableItemDrum {
    int getDefaultSize();
    int getMaximumSize();
    int getSize();
    void setSize(int size);
}
