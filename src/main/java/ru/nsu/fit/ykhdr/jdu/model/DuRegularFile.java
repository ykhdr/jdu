package ru.nsu.fit.ykhdr.jdu.model;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Record that implements the DuFile interface and describes the file structure.
 * <p>
 *
 * @param path absolute path to file.
 * @param size file size.
 */
public record DuRegularFile(
        @NotNull Path path,
        long size) implements DuFile {
}