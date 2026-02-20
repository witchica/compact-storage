package com.witchica.compactstorage.api;

import com.witchica.compactstorage.data.StorageType;
import org.jspecify.annotations.NonNull;

public interface StorageTypeProvider {
    public @NonNull StorageType getStorageType();
}
