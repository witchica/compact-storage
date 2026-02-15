package com.witchica.compactstorage.util;

import org.jspecify.annotations.NonNull;
import util.StorageTypes;

public interface StorageTypeProvider {
    public @NonNull StorageTypes getStorageType();
}
