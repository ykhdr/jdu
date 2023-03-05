package ru.nsu.fit.ykhdr.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

/**
 * Record that implements the DuFile interface and describes the symbolic link structure.
 * <p>
 * @param path
 *        absolute path to symlink.
 * @param children
 *        child files of a symlink. Can be null if symlink point to file or empty directory.
 * @param size
 *        the size of the file pointed to by the symlink.
 */
public record DuSymlink(@NotNull Path path, @Nullable List<DuFile> children, long size) implements DuFile {

    @Override
    public @Nullable List<DuFile> children() {
        return children;
    }
    
    @Override
    public long size() {
        return size;
    }

    @Override
    public @NotNull String name() {
        return path.toFile().getName();
    }

    @Override
    public @NotNull Path path() {
        return path;
    }
}