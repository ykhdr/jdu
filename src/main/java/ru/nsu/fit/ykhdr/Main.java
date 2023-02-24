package ru.nsu.fit.ykhdr;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.model.Directory;
import ru.nsu.fit.ykhdr.model.DuConfig;
import ru.nsu.fit.ykhdr.parser.CommandLineParser;

import java.io.File;

/*
jdu [-options] dir
    options:
        - --depth n     - recursive depth
        - -L            - follow symlinks
        - --limit n     - show n the heaviest files and/or dirs (has default limit)



Что нужно?
    парсер входных команд
    нужен какой то обьект, содержащий информацию о таком дереве (как будет выглядеть дерево?)
    file.listFiles() - дает массив файлов. Рекурсивно проходится?


    Нужен какой то класс, содержащий options, с которым всегда можно сравнить

 */
public class Main {


    public static void main(String[] args) {


        if (args.length == 0) {
            System.out.println(usage());
            return;
        }

        // Argument parsing
        CommandLineParser parser = new CommandLineParser(args);
        DuConfig config = parser.parse();


        System.out.println(config.toString());

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