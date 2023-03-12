package ru.nsu.fit.ykhdr.jdu;

import ru.nsu.fit.ykhdr.jdu.exception.DuArgumentException;
import ru.nsu.fit.ykhdr.jdu.exception.DuException;
import ru.nsu.fit.ykhdr.jdu.exception.DuNumberFormatException;
import ru.nsu.fit.ykhdr.jdu.model.DuFile;
import ru.nsu.fit.ykhdr.jdu.options.DuOptions;
import ru.nsu.fit.ykhdr.jdu.options.OptionsBuilder;
import ru.nsu.fit.ykhdr.jdu.utils.DuTreeBuilder;
import ru.nsu.fit.ykhdr.jdu.utils.DuTreePrinter;

public class Main {

    public static void main(String[] args) {
        OptionsBuilder optionsBuilder = new OptionsBuilder(args);

        try {
            DuOptions options = optionsBuilder.build();

            DuTreeBuilder treeBuilder = new DuTreeBuilder();
            DuTreePrinter printer = new DuTreePrinter(System.out, options.limit(), options.depth(), options.followSymlinks());

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