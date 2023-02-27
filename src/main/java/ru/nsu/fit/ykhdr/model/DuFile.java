package ru.nsu.fit.ykhdr.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public sealed interface DuFile permits DuDirectory, DuRegularFile, DuSymlink {
    @Nullable List<DuFile> children();

    long size();

    @NotNull String name();
}
