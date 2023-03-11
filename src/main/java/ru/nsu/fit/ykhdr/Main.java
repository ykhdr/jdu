package ru.nsu.fit.ykhdr;

import ru.nsu.fit.ykhdr.exception.DuArgumentException;
import ru.nsu.fit.ykhdr.exception.DuException;
import ru.nsu.fit.ykhdr.exception.DuNumberFormatException;
import ru.nsu.fit.ykhdr.model.DuFile;
import ru.nsu.fit.ykhdr.options.DuOptions;
import ru.nsu.fit.ykhdr.options.OptionsBuilder;
import ru.nsu.fit.ykhdr.utils.TreeBuilder;
import ru.nsu.fit.ykhdr.utils.TreePrinter;

public class Main {

    public static void main(String[] args) {
        OptionsBuilder optionsBuilder = new OptionsBuilder(args);

        try {
            DuOptions options = optionsBuilder.build();

            TreeBuilder treeBuilder = new TreeBuilder();
            TreePrinter printer = new TreePrinter(options.limit(), options.depth(), options.followSymlinks());

            DuFile rootDir = treeBuilder.build(options.root());
            printer.print(rootDir);

        } catch (DuArgumentException | DuNumberFormatException e) {
            System.err.println(e.getMessage());
            System.err.println(usage());
        } catch (DuException e) {
            System.err.println(e.getMessage());
        }
    }

    static String usage() {
        return """
                jdu [-options] dir
                    dir - root directory for scanning (has default current)
                   
                    options:
                        - --depth n     - recursive depth (has default limit = 10)
                        - -L            - follow symlinks
                        - --limit n     - show n the heaviest files and/or dirs (has default limit = 5)
                """;
    }
}