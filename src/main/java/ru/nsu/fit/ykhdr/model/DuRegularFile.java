package ru.nsu.fit.ykhdr.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

public record DuRegularFile(@NotNull Path path, long size) implements DuFile {

    @Override
    public @Nullable List<DuFile> children() {
        return null;
    }

    @Override
    public @NotNull String name() {
        return path.toFile().getName();
    }

    public @NotNull Path path() {
        return path;
    }
}