package ru.nsu.fit.ykhdr.utils;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.model.DuDirectory;
import ru.nsu.fit.ykhdr.model.DuFile;
import ru.nsu.fit.ykhdr.model.DuRegularFile;

import java.util.List;

public class TreePrinter {
    public static void print(@NotNull DuFile rootDir) {
        printDir(rootDir, 0);
    }

    private static void print(@NotNull DuFile curDuFile, int depth) {
        List<DuFile> children = curDuFile.children();

        if (children == null) {
            return;
        }

        for (DuFile child : children) {
            if (child instanceof DuDirectory) {
                printDir(child, depth);
            }
            else if (child instanceof DuRegularFile) {
                printFile(child, depth);
            }

        }
    }

    private static void printDir(@NotNull DuFile dir, int depth) {
        System.out.println("\t".repeat(depth) + "/" + dir.name() + " [" + SizeConverter.convertToString(dir.size()) + "]");
        print(dir, depth + 1);
    }

    private static void printFile(@NotNull DuFile file, int depth) {
        System.out.println("\t".repeat(depth) + file.name() + " [" + SizeConverter.convertToString(file.size()) + "]");
    }

}
