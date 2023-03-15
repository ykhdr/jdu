package ru.nsu.fit.ykhdr.jdu.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DuCompoundFile {
    @NotNull List<DuFile> children();
}
