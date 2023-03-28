package ru.nsu.fit.ykhdr.jdu.model;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public record DuUnknownFile(@NotNull Path path) implements DuFile{
    @Override
    public long size() {
        return 0;
    }

    @Override
    public @NotNull Path path() {
        return path;
    }
}
