package ru.nsu.fit.ykhdr.options;

import java.nio.file.Path;

public record DuOptions(
        Path root,
        int depth,
        int limit,
        boolean followSymlinks
) {}