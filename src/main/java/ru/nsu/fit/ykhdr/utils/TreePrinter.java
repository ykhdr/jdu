package ru.nsu.fit.ykhdr.utils;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.model.DuDirectory;
import ru.nsu.fit.ykhdr.model.DuFile;
import ru.nsu.fit.ykhdr.model.DuRegularFile;
import ru.nsu.fit.ykhdr.model.DuSymlink;

import java.util.List;

public class TreePrinter {
    private enum ConsoleColor {
        TEXT_RESET("\u001B[0m"),
        TEXT_CYAN("\u001B[36m"),
        TEXT_GREEN("\u001B[32m");

        private final String color;

        ConsoleColor(String color) {
            this.color = color;
        }
    }

    public static void print(@NotNull DuFile rootDir) {
        printDirectory(rootDir, 0);
    }

    private static void print(@NotNull DuFile curDuFile, int depth) {
        List<DuFile> children = curDuFile.children();

        if (children == null) {
            return;
        }

        for (DuFile child : children) {
            if (child instanceof DuDirectory) {
                printDirectory(child, depth);
            }
            else if (child instanceof DuRegularFile) {
                printRegularFile(child, depth);
            }
            else if (child instanceof DuSymlink) {
                printSymlink(child, depth);
            }
        }
    }

    private static void printDirectory(@NotNull DuFile dir, int depth) {
        System.out.println("\t".repeat(depth)  + coloredText("/" + dir.name(), ConsoleColor.TEXT_GREEN) + size(dir));
        print(dir, depth + 1);
    }

    private static void printRegularFile(@NotNull DuFile file, int depth) {
        System.out.println("\t".repeat(depth) + file.name() + size(file));
    }

    private static void printSymlink(@NotNull DuFile link, int depth) {
        System.out.println("\t".repeat(depth) + coloredText(link.name() + "@",ConsoleColor.TEXT_CYAN) + size(link));
        print(link, depth);
    }

    private static @NotNull String size(@NotNull DuFile file) {
        return " [" + SizeConverter.convertToString(file.size()) + "]";
    }

    private static @NotNull String coloredText(@NotNull String text, @NotNull ConsoleColor color){
        return color.color + text + ConsoleColor.TEXT_RESET.color;
    }
}
