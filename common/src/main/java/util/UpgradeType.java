package util;

public enum UpgradeType {
    WIDTH_UPGRADE, // Add a slot to the x axis
    HEIGHT_UPGRADE, // Add a slot to the y axis
    RETAINING_UPGRADE, // Make block / barrel keep contents when destroyed
    VOID_SLOT_UPGRADE, // Add a void slot to the inventory
    CHUNK_LOADER_UPGRADE, // Make the chest load a chunk
    NAME_TAG_UPGRADE // Display the chest name above it
}
