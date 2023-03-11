package ru.nsu.fit.ykhdr.options;

import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.exception.DuArgumentException;
import ru.nsu.fit.ykhdr.exception.DuNumberFormatException;

import java.nio.file.Path;

import org.apache.commons.cli.*;

/**
 * The class, based on the command line arguments given to it, creates a DuOptions object and returns it.
 */

public class OptionsBuilder {
    private final String[] args;

    private int depth = 10;
    private int limit = 5;
    private boolean followSymlink = false;

    private static final Options DU_OPTIONS;

    static {
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

        DU_OPTIONS = new Options().addOption(depthOption).addOption(limitOption).addOption(followSymlinkOption);
    }

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
        CommandLine cmdLine = createCommandLine(args);

        if (cmdLine.hasOption("depth")) {
            depth = parseInt(cmdLine.getOptionValue("depth"));
        }
        if (cmdLine.hasOption("limit")) {
            limit = parseInt(cmdLine.getOptionValue("limit"));
        }
        if (cmdLine.hasOption("L")) {
            followSymlink = true;
        }

        Path rootPath = getPath(cmdLine);

        return new DuOptions(rootPath, depth, limit, followSymlink);
    }
    private static @NotNull CommandLine createCommandLine(@NotNull String[] args) {
        try {
            return new DefaultParser().parse(DU_OPTIONS, args);
        }
        catch (ParseException e) {
            throw new DuArgumentException(e);
        }
    }

    private @NotNull Path getPath(@NotNull CommandLine cmdLine) {
        if (cmdLine.getArgList().size() > 1) {
            throw new DuArgumentException("Multiple directories entered");
        }

        String path = cmdLine.getArgList().isEmpty() ? "./" : cmdLine.getArgList().get(0);
        return Path.of(path);
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