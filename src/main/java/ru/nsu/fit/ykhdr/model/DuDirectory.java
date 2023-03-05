package ru.nsu.fit.ykhdr.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

/**
 * Record that implements the DuFile interface and describes the directory structure.
 * <p>
 * @param path
 *        absolute path to directory.
 * @param children
 *        child files of a directory. Can be null if directory is empty.
 * @param size
 *        directory size.
 */
public record DuDirectory(@NotNull Path path, @Nullable List<DuFile> children, long size) implements DuFile {

    @Override
    public @Nullable List<DuFile> children() {
        return children;
    }

    @Override
    public @NotNull String name() {
        return path.toFile().getName();
    }

    public @NotNull Path path() {
        return path;
    }
}