package ru.nsu.fit.ykhdr.jdu.model;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

/**
 * Represents a directory in the file system.
 */
public record DuDirectory(@NotNull Path path, @NotNull List<DuFile> children, long size) implements DuFile, DuCompoundFile {
}