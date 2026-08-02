package com.witchica.compactstorage.api.inventory;

/**
 * Remembers the last sort key/arrangement actually applied to a storage (packed as
 * key.ordinal() * SortArrangement.values().length + arrangement.ordinal() so this interface
 * doesn't need to depend on GenericCompactStorageMenu's enums), so reopening the same storage
 * doesn't forget what it was last sorted by.
 */
public interface SortPreferenceContainer {
    int getSortPreference();
    void setSortPreference(int packedPreference);
}
