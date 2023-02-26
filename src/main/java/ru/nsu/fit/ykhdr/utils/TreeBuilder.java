package ru.nsu.fit.ykhdr.utils;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.exception.DuIOException;
import ru.nsu.fit.ykhdr.model.DuDirectory;
import ru.nsu.fit.ykhdr.model.DuFile;
import ru.nsu.fit.ykhdr.model.DuRegularFile;
import ru.nsu.fit.ykhdr.model.DuSymlink;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TreeBuilder {
    private static int maxDepth;

    public static @NotNull DuFile build(Path root, int maxDepth) {
        TreeBuilder.maxDepth = maxDepth;

        try {
            return build(root, new HashSet<>(), 0);
        }
        catch (IOException e) {
            throw new DuIOException(e);
        }
    }

    private static @NotNull DuFile build(Path curPath, Set<DuFile> visited, int depth) throws IOException {
        if (Files.isDirectory(curPath)) {
            int size = 0;
            List<Path> childrenList = Files.list(curPath).toList();
            List<DuFile> children = new ArrayList<>();
            for (Path childPath : childrenList) {
                // TODO: сделать проверку на visited, а также учитывать size;
                DuFile childrenFile = build(childPath, visited, depth + 1);
                size += childrenFile.size();
                children.add(childrenFile);
            }
            if (depth >= maxDepth) {
                return new DuDirectory(curPath.toFile().getName(), null, size);
            }

            return new DuDirectory(curPath.toFile().getName(), children, size);
        }
        else if (Files.isRegularFile(curPath)) {
            return new DuRegularFile(curPath.toFile().getName(), curPath.toFile().length());
        }
//        else if (Files.isSymbolicLink(curPath)){
//            // TODO: 25.02.2023 сделать........
//            return new DuSymlink();
//        }
        else {
            throw new IOException("wrong....");
        }
    }
}
