package ru.nsu.fit.ykhdr.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record DuDirectory(String name, List<DuFile> children, long size) implements DuFile {
    public DuDirectory(@NotNull String name, @Nullable List<DuFile> children, long size) {
        this.name = name;
        this.children = children;
        this.size = size;
    }

    @Override
    public @Nullable List<DuFile> children() {
        return children;
    }

    @Override
    public @NotNull String name() {
        return name;
    }

}
