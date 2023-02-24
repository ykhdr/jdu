package ru.nsu.fit.ykhdr.parser;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.model.Directory;
import ru.nsu.fit.ykhdr.model.DuConfig;

import java.io.File;

public class CommandLineParser {
    private Directory root = null;
    private int depth = 5;
    private int limit = 0;
    private boolean followSymlinks = false;
    private final String[] args;

    public CommandLineParser(@NotNull String[] args) {
        this.args = args;
    }

    public @NotNull DuConfig parse() {
        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-') {
                switch (args[i]) {
                    case "-L" -> setFollowSymlink();
                    case "--limit" -> setLimit(++i);
                    case "--depth" -> setDepth(++i);
                    default -> throw new RuntimeException("Unknown option entered");
                }
            }
            else {
                setRoot(args[i]);
            }
        }

        //TODO: сделать проверку на то что root остался пустым и заполнить его дефолтным значением

        return new DuConfig(root, depth, limit, followSymlinks);
    }

    private void setDepth(int i) {
        try {
            depth = parseInt(args[i]);
        }
        catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    private void setLimit(int i) {
        try {
            limit = parseInt(args[i]);
        }
        catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFollowSymlink() {
        followSymlinks = true;
    }

    private void setRoot(@NotNull String path) {
        if (root != null) {
            throw new RuntimeException("Re-entry root dir");
        }

        File rootFile = new File(path);

        if (!rootFile.exists()) {
            throw new RuntimeException("Root dir doesn't exists");
        }
        if (rootFile.isFile()) {
            throw new RuntimeException("Root argument isn't dir");
        }

        root = new Directory(rootFile);
    }

    private static int parseInt(String str) {
        try {
            int num = Integer.parseInt(str);
            if (num < 0) {
                throw new RuntimeException("Incorrectly number entered : " + str);
            }
            return num;
        }
        catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
