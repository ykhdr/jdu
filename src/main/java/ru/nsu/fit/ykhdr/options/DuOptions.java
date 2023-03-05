package ru.nsu.fit.ykhdr.options;

import java.nio.file.Path;

/**
 * Record containing all command line options.
 * <p>
 * @param root
 *        root directory for building the tree.
 * @param depth
 *        tree depth.
 * @param limit
 *        the number of the heaviest directories displayed for each level.
 * @param followSymlinks
 *        flag indicating the need to follow symlinks
 */

public record DuOptions(
        Path root,
        int depth,
        int limit,
        boolean followSymlinks
) {}