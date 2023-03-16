package ru.nsu.fit.ykhdr.jdu.utils;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.jdu.exception.DuIOException;
import ru.nsu.fit.ykhdr.jdu.model.*;
import ru.nsu.fit.ykhdr.jdu.printformat.*;

import java.io.PrintStream;
import java.util.*;

/**
 * Utility class that prints the directory given to it.
 */

public class DuTreePrinter {
    private final PrintStream printStream;

    private final int limit;
    private final int maxDepth;
    private final boolean followSymlinks;

    private static final Comparator<DuFile> COMPARATOR = new DuComparator();

    public DuTreePrinter(@NotNull PrintStream printStream, int limit, int maxDepth, boolean followSymlinks) {
        this.printStream = printStream;
        this.limit = limit;
        this.maxDepth = maxDepth;
        this.followSymlinks = followSymlinks;
    }

    /**
     * Prints the tree of the given directory.
     * <p>
     *
     * @param root DuFile root directory for print.
     *             <p>
     * @throws DuIOException if the symlink target is not available or set incorrectly.
     */
    public void print(@NotNull DuFile root) {
        print(root, 0);
    }

    private void printCompoundFile(@NotNull DuCompoundFile curDuFile, int depth) {
        if (depth > maxDepth) {
            return;
        }

        List<DuFile> subChildren = subLimitList(curDuFile.children());

        for (DuFile child : subChildren) {
            print(child, depth);
        }
    }

    private void print(@NotNull DuFile file, int depth) {
        switch (file) {
            case DuDirectory directory -> printDirectory(directory, depth);
            case DuSymlink symlink -> printSymlink(symlink, depth);
            case DuRegularFile regularFile -> printRegularFile(regularFile, depth);
        }
    }

    private void printDirectory(@NotNull DuDirectory dir, int depth) {
        printStream.println(FileRepresentation.format(FormattingTemplates.DIR_TEMPLATE, dir, depth));

        printCompoundFile(dir, depth + 1);
    }

    private void printRegularFile(@NotNull DuRegularFile file, int depth) {
        printStream.println(FileRepresentation.format(FormattingTemplates.FILE_TEMPLATE, file, depth));
    }

    private void printSymlink(@NotNull DuSymlink link, int depth) {
        printStream.println(FileRepresentation.format(FormattingTemplates.SYM_TEMPLATE, link, depth));

        if (followSymlinks) {
            printCompoundFile(link, depth + 1);
        }
    }

    private @NotNull List<DuFile> subLimitList(@NotNull List<DuFile> list) {
        List<DuFile> copy = new ArrayList<>(list);
        copy.sort(COMPARATOR);
        return copy.subList(0, Math.min(limit, copy.size()));
    }
}