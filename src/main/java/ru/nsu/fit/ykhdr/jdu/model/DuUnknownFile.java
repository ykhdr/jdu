package ru.nsu.fit.ykhdr.jdu.model;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

// CR: comment
public record DuUnknownFile(@NotNull Path path, long size) implements DuFile{
    @Override
    public long size() {
        return size;
    }

    @Override
    public @NotNull Path path() {
        return path;
    }
}
