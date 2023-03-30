package ru.nsu.fit.ykhdr.jdu.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents a compound file that contains other files as its children.
 */
public interface DuCompoundFile {

    /**
     * Gets the list of child files contained within this compound file.
     *
     * @return the list of child files contained within this compound file.
     */
    @NotNull List<DuFile> children();
}
