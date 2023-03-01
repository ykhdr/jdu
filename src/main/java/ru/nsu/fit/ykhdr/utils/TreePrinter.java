package ru.nsu.fit.ykhdr.utils;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.exception.DuIOException;
import ru.nsu.fit.ykhdr.model.DuDirectory;
import ru.nsu.fit.ykhdr.model.DuFile;
import ru.nsu.fit.ykhdr.model.DuRegularFile;
import ru.nsu.fit.ykhdr.model.DuSymlink;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class TreePrinter {
    private enum ConsoleColor {
        TEXT_RESET("\u001B[0m"),
        TEXT_CYAN("\u001B[36m"),
        TEXT_BRIGHT_CYAN("\u001B[96m"),
        TEXT_GREEN("\u001B[32m");

        private final String color;

        ConsoleColor(String color) {
            this.color = color;
        }
    }

    private static int fileLimit = 5;

    public static void printTree(@NotNull DuFile rootDir, int limit) {
        fileLimit = limit;
        printDirectory(rootDir, 0);
    }

    private static void print(@NotNull DuFile curDuFile, int depth) {
        List<DuFile> children = curDuFile.children();

        if (children == null) {
            return;
        }

        children = subLimitList(children);

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
        System.out.println("\t".repeat(depth) +
                coloredText("/" + dir.name(), ConsoleColor.TEXT_GREEN) +
                size(dir));
        print(dir, depth + 1);
    }

    private static void printRegularFile(@NotNull DuFile file, int depth) {
        System.out.println("\t".repeat(depth) +
                file.name() +
                size(file));
    }

    private static void printSymlink(@NotNull DuFile link, int depth) {
        System.out.println("\t".repeat(depth) +
                coloredText(link.name() + "@", ConsoleColor.TEXT_CYAN) + " -> " +
                symlinkTarget(link) +
                size(link));

        print(link, depth + 1);
    }

    private static @NotNull String symlinkTarget(@NotNull DuFile link) {
        return coloredText(readSymlinkTarget(link).toString(), ConsoleColor.TEXT_BRIGHT_CYAN);
    }

    private static @NotNull Path readSymlinkTarget(@NotNull DuFile link) {
        try {
            return Files.readSymbolicLink(link.path());
        }
        catch (IOException e) {
            throw new DuIOException(e);
        }
    }

    private static @NotNull List<DuFile> subLimitList(List<DuFile> list){
        list.sort(Comparator.comparing(DuFile::size).reversed());
        return list.subList(0, Math.min(fileLimit, list.size()));
    }

    private static @NotNull String size(@NotNull DuFile file) {
        return " [" + SizeConverter.convertToString(file.size()) + "]";
    }

    private static @NotNull String coloredText(@NotNull String text, @NotNull ConsoleColor color) {
        return color.color + text + ConsoleColor.TEXT_RESET.color;
    }
}