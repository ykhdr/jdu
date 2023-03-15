package ru.nsu.fit.ykhdr.jdu.model;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

/**
 * Record that implements the DuFile interface and describes the symbolic link structure.
 * <p>
 *
 * @param path     absolute path to symlink.
 * @param children child files of a symlink. Can be null if symlink point to file or empty directory.
 * @param size     the size of the file pointed to by the symlink.
 */
public record DuSymlink(@NotNull Path path, @NotNull List<DuFile> children, long size) implements DuFile, DuCompoundFile {
}