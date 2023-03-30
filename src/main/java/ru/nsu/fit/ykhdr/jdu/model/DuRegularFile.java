package ru.nsu.fit.ykhdr.jdu.model;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Represents a regular file in the file system
 */
public record DuRegularFile(@NotNull Path path, long size) implements DuFile {
}