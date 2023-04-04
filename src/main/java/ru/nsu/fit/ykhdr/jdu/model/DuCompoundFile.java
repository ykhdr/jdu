package ru.nsu.fit.ykhdr.jdu.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a compound file that contains other files as its children.
 */
public interface DuCompoundFile {
    @NotNull List<DuFile> children();
}
