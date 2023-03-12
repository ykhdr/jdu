package ru.nsu.fit.ykhdr.jdu.options;

import java.nio.file.Path;

/**
 * Record containing all command line options.
 * <p>
 * @param root
 *        root directory for building the tree.
 * @param depth
 *        tree depth.
 * @param limit
 *        the number of the heaviest directories displayed for each level for each directory.
 * @param followSymlinks
 *        flag indicating whether the contents of the symlink should be printed.
 */

public record DuOptions(
        Path root,
        int depth,
        int limit,
        boolean followSymlinks
) {}