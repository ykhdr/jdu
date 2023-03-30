package ru.nsu.fit.ykhdr.jdu.options;

import org.apache.commons.cli.Options;
import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.jdu.exception.DuArgumentException;
import ru.nsu.fit.ykhdr.jdu.exception.DuNumberFormatException;

import java.nio.file.Path;

import org.apache.commons.cli.*;

/**
 * A utility class for building DuOptions instances from command line arguments.
 */

public class OptionsBuilder {

    private int depth = 10;
    private int limit = 5;
    private boolean followSymlink = false;

    private static final Options DU_OPTIONS;

    private OptionsBuilder() {
    }

    static {
        Option depthOption = Option.builder()
                .longOpt("depth").
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
     * Parses command line arguments and constructs a new instance of DuOptions.
     * <p>
     *
     * @return A new instance of DuOptions representing the parsed arguments.
     * @throws DuArgumentException     if given on the command line options are incorrectly.
     * @throws DuNumberFormatException if numeric argument parameters are not numbers.
     */
    public static @NotNull DuOptions build(String[] args) {
        return new OptionsBuilder().buildImpl(args);
    }

    private @NotNull DuOptions buildImpl(String[] args) {
        CommandLine cmdLine = createCommandLine(args);

        Path rootPath = getPath(cmdLine);

        if (cmdLine.hasOption("depth")) {
            depth = parseInt(cmdLine.getOptionValue("depth"));
        }
        if (cmdLine.hasOption("limit")) {
            limit = parseInt(cmdLine.getOptionValue("limit"));
        }
        if (cmdLine.hasOption("L")) {
            followSymlink = true;
        }
        return new DuOptions(rootPath, depth, limit, followSymlink);
    }

    private static @NotNull CommandLine createCommandLine(@NotNull String[] args) {
        try {
            return new DefaultParser().parse(DU_OPTIONS, args);
        } catch (ParseException e) {
            throw new DuArgumentException(e.getMessage());
        }
    }

    private @NotNull Path getPath(@NotNull CommandLine cmdLine) {
        if (cmdLine.getArgList().size() > 1) {
            throw new DuArgumentException("Multiple root path entered");
        }

        return Path.of(cmdLine.getArgList().isEmpty() ? "./" : cmdLine.getArgList().get(0));
    }

    private static int parseInt(@NotNull String str) {
        try {
            int num = Integer.parseInt(str);
            if (num < 1) {
                throw new DuNumberFormatException("incorrectly number entered", num);
            }
            return num;
        } catch (NumberFormatException e) {
            throw new DuNumberFormatException("this parameter isn't a number", str);
        }
    }
}