package ru.nsu.fit.ykhdr.model;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

/**
 * Record that implements the DuFile interface and describes the directory structure.
 * <p>
 *
 * @param path     absolute path to directory.
 * @param children child files of a directory. Can be null if directory is empty.
 * @param size     directory size.
 */
public record DuDirectory(
        @NotNull Path path,
        @NotNull List<DuFile> children,
        long size) implements DuFile, DuCompoundFile {
}