package ru.nsu.fit.ykhdr.parser;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.exception.DuArgumentException;
import ru.nsu.fit.ykhdr.exception.DuNumberFormatException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class CommandLineParser {

    private final List<String> args;

    private Path rootPath;
    private int depth = 5;
    private int limit = 0;
    private boolean followSymlink = false;

    public CommandLineParser(String[] args) {
        this.args = Arrays.asList(args);
    }

    public @NotNull DuConfig createConfig() {
        setArguments();
        return new DuConfig(rootPath, depth, limit, followSymlink);
    }

    private void setArguments() {
        boolean skipValue = false;

        for (String arg : args) {
            if (skipValue) {
                skipValue = false;
                continue;
            }
            if (arg.startsWith("-")) {
                switch (arg) {
                    case "-L" -> setFollowSymlink();
                    case "--limit" -> {
                        setLimit(nextArgument(arg));
                        skipValue = true;
                    }
                    case "--depth" -> {
                        setDepth(nextArgument(arg));
                        skipValue = true;
                    }
                    default -> throw new DuArgumentException("unknown option", arg);
                }
            }
            else {
                setRootPath(arg);
            }
        }
        if (rootPath == null) {
            rootPath = Paths.get("./");
        }

    }

    private String nextArgument(String currArg) {
        try {
            return args.get(args.indexOf(currArg) + 1);
        }
        catch (IndexOutOfBoundsException e) {
            throw new DuArgumentException("option must have parameter", currArg);
        }
    }

    private void setRootPath(String root) {
        if (rootPath != null) {
            throw new DuArgumentException("root entered twice", root);
        }

        rootPath = Path.of(root);
    }

    private void setFollowSymlink() {
        followSymlink = true;
    }

    private void setLimit(@NotNull String arg) {
        limit = parseInt(arg);
    }

    private void setDepth(@NotNull String arg) {
        depth = parseInt(arg);
    }

    private static int parseInt(@NotNull String str) {
        try {
            int num = Integer.parseInt(str);
            if (num < 0) {
                throw new DuNumberFormatException("incorrectly number entered", num);
            }
            return num;
        }
        catch (NumberFormatException e) {
            throw new DuNumberFormatException("this parameter isn't a number", str);
        }
    }
}
