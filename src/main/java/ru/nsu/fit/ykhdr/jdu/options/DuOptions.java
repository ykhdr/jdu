package ru.nsu.fit.ykhdr.jdu.options;

import java.nio.file.Path;

/**
 * Represents parsed command line arguments for the du command.
 * <p/>
 *
 * @param root The root directory to start scanning from.
 * @param depth The maximum depth to scan.
 * @param limit The maximum number of files to include in the result.
 * @param followSymlinks Whether to follow symbolic links.
 */
public record DuOptions(Path root, int depth, int limit, boolean followSymlinks) {
}