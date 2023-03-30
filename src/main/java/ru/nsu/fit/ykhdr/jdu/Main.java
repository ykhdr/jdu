package ru.nsu.fit.ykhdr.jdu;

import ru.nsu.fit.ykhdr.jdu.exception.DuArgumentException;
import ru.nsu.fit.ykhdr.jdu.exception.DuException;
import ru.nsu.fit.ykhdr.jdu.exception.DuIOException;
import ru.nsu.fit.ykhdr.jdu.model.DuFile;
import ru.nsu.fit.ykhdr.jdu.options.DuOptions;
import ru.nsu.fit.ykhdr.jdu.options.OptionsBuilder;
import ru.nsu.fit.ykhdr.jdu.utils.DuLinker;
import ru.nsu.fit.ykhdr.jdu.utils.DuTreeBuilder;
import ru.nsu.fit.ykhdr.jdu.utils.DuTreePrinter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        try {
            DuOptions options = OptionsBuilder.build(args);

            if (!Files.exists(options.root())) {
                throw new DuArgumentException("File doesn't exist: " + options.root());
            }

            DuTreeBuilder treeBuilder = new DuTreeBuilder();
            DuTreePrinter printer = new DuTreePrinter(System.out, options.limit(), options.depth(), options.followSymlinks());

            Map<Path, DuFile> treeNodes = new HashMap<>();
            DuFile root = treeBuilder.buildTree(options.root(), treeNodes);

            if (options.followSymlinks()) {
                // CR: call from builder
                DuLinker.linkSymlinks(treeNodes);
            }

            printer.print(root);
        } catch (DuIOException e) {
            System.err.println(e.getMessage());
        } catch (DuException e) {
            System.err.println(e.getMessage());
            System.err.println(usage());
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