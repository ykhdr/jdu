package ru.nsu.fit.ykhdr.model;

import ru.nsu.fit.ykhdr.model.Directory;

public record DuConfig(
        Directory root,
        int depth,
        int limit,
        boolean followSymlinks
) {}