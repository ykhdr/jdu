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
import java.util.stream.Stream;

public class TreeBuilder {
    private static int maxDepth;

    private static boolean followSymlink;

    public static @NotNull DuFile build(Path root, int maxDepth, boolean followSymlink) {
        TreeBuilder.maxDepth = maxDepth;
        TreeBuilder.followSymlink = followSymlink;

        return build(root, 0);
    }

    // TODO: 27.02.2023 реализовать visited с проверкой на то есть ли там уже такой файл.
    private static @NotNull DuFile build(Path curPath, int depth) {
        if (Files.isSymbolicLink(curPath)) {
            // TODO: 25.02.2023 сделать........
            return buildSymlink(curPath, depth);
        }
        else if (Files.isDirectory(curPath)) {
            return buildDirectory(curPath, new HashSet<>(), depth);
        }
        else if (Files.isRegularFile(curPath)) {
            return buildRegularFile(curPath);
        }
        else {
            throw new DuIOException("Unknown file is in this directory : " + curPath.toFile().getAbsolutePath());
        }

    }

    private static @NotNull DuDirectory buildDirectory(@NotNull Path path, @NotNull Set<DuFile> visited, int depth) {
        List<DuFile> children = children(path, depth);
        children.removeIf(visited::contains);

//        visited.contains()

        long size = size(children);

        if (depth >= maxDepth || children.isEmpty()) {
            return new DuDirectory(path.toFile().getName(), null, size);
        }

        return new DuDirectory(path.toFile().getName(), children, size);
    }

    private static @NotNull DuRegularFile buildRegularFile(@NotNull Path path) {
        return new DuRegularFile(path.toFile().getName(), path.toFile().length());
    }

    private static @NotNull DuDirectory buildSymlink(@NotNull Path path, int depth) {

//        if (Files.isDirectory(path)) {
//            List<DuFile> children = children(path,depth);
//            if(depth >= maxDepth){
//                return new DuSymlink(path.toFile().getName(),null,);
//            }
//        }

        //return new DuSymlink(path.toFile().getName(), null, 0);
        return new DuDirectory(path.toFile().getName(), children(path,depth), size(children(path,depth)));
    }

    private static @NotNull List<DuFile> children(@NotNull Path path, int depth) {
        List<DuFile> children = new ArrayList<>();

        try (Stream<Path> childrenStream = Files.list(path)) {
            List<Path> childrenList = childrenStream.toList();

            for (Path childPath : childrenList) {
                DuFile childrenFile = build(childPath, depth + 1);
                children.add(childrenFile);
            }
        }
        catch (IOException e) {
            throw new DuIOException(e);
        }

        return children;
    }

    private static long size(@NotNull List<DuFile> children) {
        long size = 0;

        for (DuFile child : children) {
            size += child.size();
        }

        return size;
    }

}
