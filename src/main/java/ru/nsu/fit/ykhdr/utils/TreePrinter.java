package ru.nsu.fit.ykhdr.utils;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.exception.DuIOException;
import ru.nsu.fit.ykhdr.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class that prints the directory given to it.
 */

public class TreePrinter {
    private final int limit;
    private final int maxDepth;
    private final boolean followSymlinks;
    private static final String INDENTATION = "  ";
    private static final Comparator<DuFile> COMPARATOR = new DuComparator();

    public TreePrinter(int limit, int maxDepth, boolean followSymlinks) {
        this.limit = limit;
        this.maxDepth = maxDepth;
        this.followSymlinks = followSymlinks;
    }

    /**
     * Prints the tree of the given directory.
     * <p>
     *
     * @param root  DuFile root directory for print.
     * <p>
     * @throws DuIOException if the symlink target is not available or set incorrectly.
     */
    public void print(@NotNull DuFile root) {
        choosePrint(root, 0);
    }

    private void print(@NotNull DuCompoundFile curDuFile, int depth) {
        if (depth > maxDepth) {
            return;
        }

        List<DuFile> subChildren = subLimitList(curDuFile.children());

        for (DuFile child : subChildren) {
            choosePrint(child, depth);
        }
    }

    private void choosePrint(@NotNull DuFile file, int depth) {
        switch (file) {
            case DuDirectory directory -> printDirectory(directory, depth);
            case DuSymlink symlink -> printSymlink(symlink, depth);
            case DuRegularFile regularFile -> printRegularFile(regularFile, depth);
        }
    }

    private void printDirectory(@NotNull DuDirectory dir, int depth) {
        System.out.println(
                INDENTATION.repeat(depth) +
                        "/" +
                        dir.name() +
                        size(dir));

        print(dir, depth + 1);
    }

    private void printRegularFile(@NotNull DuRegularFile file, int depth) {
        System.out.println(
                INDENTATION.repeat(depth) +
                        file.name() +
                        size(file));
    }

    private void printSymlink(@NotNull DuSymlink link, int depth) {
        System.out.println(
                INDENTATION.repeat(depth) +
                        link.name() +
                        "@ -> " +
                        symlinkTarget(link) +
                        size(link));

        if (followSymlinks) {
            print(link, depth + 1);
        }
    }

    private static @NotNull String symlinkTarget(@NotNull DuFile link) {
        return readSymlinkTarget(link).toString();
    }

    private static @NotNull Path readSymlinkTarget(@NotNull DuFile link) {
        try {
            return Files.readSymbolicLink(link.path());
        } catch (IOException e) {
            throw new DuIOException(e);
        }
    }

    private @NotNull List<DuFile> subLimitList(@NotNull List<DuFile> list) {
        list.sort(COMPARATOR);
        return list.subList(0, Math.min(limit, list.size()));
    }

    private static @NotNull String size(@NotNull DuFile file) {
        return " [" + SizeConverter.convertToString(file.size()) + "]";
    }
}