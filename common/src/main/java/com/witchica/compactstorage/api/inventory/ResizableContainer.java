package com.witchica.compactstorage.api.inventory;

import com.witchica.compactstorage.data.UpgradeType;

public interface ResizableContainer {
    int getWidth();
    int getHeight();

    int getMaximumWidth();
    int getMaximumHeight();

    void setSize(int width, int height);

    default void setWidth(int width) {
        setSize(width, getHeight());
    }

    default void setHeight(int height) {
        setSize(getWidth(), height);
    }
}
