package com.witchica.compactstorage.api.inventory;

/**
 * Opt-in per-container toggle, off by default (matching the original linear-copy resize
 * behavior exactly). Only affects width resizes - row/col math is unchanged when only height
 * grows or shrinks, so a height-only resize behaves identically either way.
 */
public interface ArrangementPreservingContainer {
    boolean preservesArrangement();
    void setPreservesArrangement(boolean preservesArrangement);
}
