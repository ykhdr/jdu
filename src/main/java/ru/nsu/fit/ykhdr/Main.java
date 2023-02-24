package ru.nsu.fit.ykhdr;

import ru.nsu.fit.ykhdr.model.DuConfig;
import ru.nsu.fit.ykhdr.parser.CommandLineParser;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(usage());
            return;
        }

        // Argument parsing
        CommandLineParser parser = new CommandLineParser(args);
        DuConfig config = parser.parse();

        DuPrinter printer = new DuPrinter(config);
        printer.printDirectoryTree();
    }

    static String usage() {
        return """
                jdu [-options] dir
                    dir - root directory for scanning (has default current)
                   
                    options:
                        - --depth n     - recursive depth
                        - -L            - follow symlinks
                        - --limit n     - show n the heaviest files and/or dirs (has default limit)
                """;
    }
}