package ru.nsu.fit.ykhdr.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

/**
 * An interface that describes a specific file type model. All classes that inherit this interface are sealed for
 * the correct operation of other utility classes.
 */
public sealed interface DuFile permits DuDirectory, DuRegularFile, DuSymlink {
    /**
     * @return List of DuFile children of file.
     */
    // CR: interface CompoundFile (children(): List<DuFile>)
    // CR: just add to DuDirectory and DuSymlink
    @Nullable List<DuFile> children();

    /**
     * @return The size of file.
     */
    long size();

    /**
     * @return Short filename.
     */
    // CR:
    @NotNull default String name() {
        return path().getFileName().toString();
    }

    /**
     * @return Absolute path of file.
     */

    @NotNull Path path();
}