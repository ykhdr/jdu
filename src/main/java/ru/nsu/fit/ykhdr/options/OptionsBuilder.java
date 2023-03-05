package ru.nsu.fit.ykhdr.options;

import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.exception.DuArgumentException;
import ru.nsu.fit.ykhdr.exception.DuNumberFormatException;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.*;

/**
 * The class, based on the command line arguments given to it, creates a DuOptions object and returns it.
 */

public class OptionsBuilder {
    private final String[] args;

    /**
     * Creates new OptionsBuilder with command line arguments.
     * <p>
     * @param args
     *        command line arguments.
     */
    public OptionsBuilder(String[] args) {
        this.args = args;
    }

    /**
     * Creates a DuOptions object based on command line arguments with filled in options
     * <p>
     * @return DuOptions with options given on the command line.
     * <p>
     * @throws DuArgumentException if given on the command line options are incorrectly.
     * @throws DuNumberFormatException if numeric argument parameters are not numbers.
     */
    public @NotNull DuOptions build() {
        Options options = setOptions();
        return parseOptions(options);
    }

    private @NotNull Options setOptions() {
        Option depthOption = Option.builder().
                longOpt("depth").
                argName("depth").
                hasArg().
                desc("recursive depth (has default limit = 10)").
                build();

        Option limitOption = Option.builder().
                longOpt("limit").
                argName("limit").
                hasArg().
                desc("show n the heaviest files and/or dirs (has default limit = 5)").
                build();

        Option followSymlinkOption = Option.builder().
                longOpt("L").
                argName("followSymlink").
                desc("follow symlinks").
                build();

        return new Options().addOption(depthOption).addOption(limitOption).addOption(followSymlinkOption);
    }

    private @NotNull DuOptions parseOptions(@NotNull Options options) {
        CommandLine cmdLine = createCommandLine(options);

        Path rootPath;
        int depth = 10;
        int limit = 5;
        boolean followSymlink = false;

        if (cmdLine.hasOption("depth")) {
            depth = parseInt(cmdLine.getOptionValue("depth"));
        }
        if (cmdLine.hasOption("limit")) {
            limit = parseInt(cmdLine.getOptionValue("limit"));
        }
        if (cmdLine.hasOption("L")) {
            followSymlink = true;
        }

        rootPath = getPath(cmdLine);

        return new DuOptions(rootPath, depth, limit, followSymlink);
    }

    private @NotNull CommandLine createCommandLine(@NotNull Options options) {
        try {
            return new DefaultParser().parse(options, args);
        }
        catch (ParseException e) {
            throw new DuArgumentException(e.getMessage());
        }
    }

    private @NotNull Path getPath(@NotNull CommandLine cmdLine) {
        if (cmdLine.getArgList().size() > 1) {
            throw new DuArgumentException("Multiple directories entered");
        }
        if (cmdLine.getArgList().isEmpty()) {
            return Path.of("./");
        }
        else {
            return Paths.get(cmdLine.getArgList().get(0));
        }
    }

    private static int parseInt(@NotNull String str) {
        try {
            int num = Integer.parseInt(str);
            if (num < 1) {
                throw new DuNumberFormatException("incorrectly number entered", num);
            }
            return num;
        }
        catch (NumberFormatException e) {
            throw new DuNumberFormatException("this parameter isn't a number", str);
        }
    }
}