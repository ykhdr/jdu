package ru.nsu.fit.ykhdr.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record DuRegularFile(String name, long size) implements DuFile {

    public DuRegularFile(@NotNull String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public @Nullable List<DuFile> children() {
        return null;
    }

    @Override
    public @NotNull String name() {
        return name;
    }

}
