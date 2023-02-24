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
                    case "-L" -> {
                        setFollowSymlink();
                    }
                    case "--limit" -> {
                        setLimit(++i);
                    }
                    case "--depth" -> {
                        setDepth(++i);
                    }
                    default -> {
                        throw new RuntimeException("Unknown option entered");
                    }
                }
            }
            else if (root == null) {
                root = createRoot(new File(args[i]));
            }
            else {
                throw new RuntimeException("Re-entry root dir");
            }
        }

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

    private static @NotNull Directory createRoot(@NotNull File rootFile) {
        if (!rootFile.exists()) {
            // TODO: нормальный и перенести usage()
            throw new RuntimeException("Root dir doesn't exists");
        }
        if (rootFile.isFile()) {
            throw new RuntimeException("Root argument isn't dir");
        }

        return new Directory(rootFile);
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
