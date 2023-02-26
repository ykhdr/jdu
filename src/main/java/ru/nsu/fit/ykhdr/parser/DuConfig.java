package ru.nsu.fit.ykhdr.parser;

import java.nio.file.Path;

public record DuConfig(
        Path root,
        int depth,
        int limit,
        boolean followSymlinks
) {}