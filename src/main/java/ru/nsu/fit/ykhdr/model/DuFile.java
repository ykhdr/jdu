package ru.nsu.fit.ykhdr.model;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * An interface that describes a specific file type model. All classes that inherit this interface are sealed for
 * the correct operation of other utility classes.
 */
public sealed interface DuFile permits DuDirectory, DuRegularFile, DuSymlink {
    /**
     * @return The size of file.
     */
    long size();

    /**
     * @return Short filename.
     */
    @NotNull default String name() {
        return path().getFileName().toString();
    }

    /**
     * @return Absolute path of file.
     */

    @NotNull Path path();
}