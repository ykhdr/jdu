package ru.nsu.fit.ykhdr.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

public record DuSymlink(@NotNull Path path, List<DuFile> children, long size) implements DuFile {

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