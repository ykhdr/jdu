package ru.nsu.fit.ykhdr.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record DuSymlink(String name, List<DuFile> children, long size) implements DuFile {

    @Override
    public @Nullable List<DuFile> children() {
        return children;
    }

    public DuSymlink(String name, List<DuFile> children, long size) {
        this.name = name;
        this.children = children;
        this.size = size;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public @NotNull String name() {
        return name;
    }


}
