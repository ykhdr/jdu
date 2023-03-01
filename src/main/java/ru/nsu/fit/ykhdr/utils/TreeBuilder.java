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
    private static Set<Path> visited;

    public static @NotNull DuFile build(Path root, int maxDepth, boolean followSymlink) {
        TreeBuilder.maxDepth = maxDepth;
        TreeBuilder.followSymlink = followSymlink;
        TreeBuilder.visited = new HashSet<>();

        DuFile fileRoot = build(root, 0);

        visited = null;
        
        return fileRoot;
    }

    private static @NotNull DuFile build(@NotNull Path curPath, int depth) {
        if (Files.isSymbolicLink(curPath)) {
            return buildSymlink(curPath, depth);
        }
        else if (Files.isDirectory(curPath)) {
            return buildDirectory(curPath, depth);
        }
        else if (Files.isRegularFile(curPath)) {
            return buildRegularFile(curPath);
        }
        else {
            throw new DuIOException("Unknown file is in this directory : " + curPath.toFile().getAbsolutePath());
        }
    }

    private static @NotNull DuDirectory buildDirectory(@NotNull Path path, int depth) {
        if(containsVisited(path)){
            return new DuDirectory(path.toAbsolutePath(),null,0);
        }

        addToVisited(path);

        List<DuFile> children = children(path, depth);
        long size = size(children);

        if (depth >= maxDepth || children.isEmpty()) {
            return new DuDirectory(path.toAbsolutePath(), null, size);
        }

        return new DuDirectory(path.toAbsolutePath(), children, size);
    }

    private static @NotNull DuRegularFile buildRegularFile(@NotNull Path path) {
        addToVisited(path);
        return new DuRegularFile(path.toAbsolutePath(), path.toFile().length());
    }

    private static @NotNull DuSymlink buildSymlink(@NotNull Path path, int depth) {
        if(containsVisited(path)){
            return new DuSymlink(path.toAbsolutePath(), null, 0);
        }

        addToVisited(path);
        List<DuFile> children = children(path, depth);
        long size = size(children);

        if (!followSymlink || depth >= maxDepth) {
            return new DuSymlink(path.toAbsolutePath(), null, size);
        }
        else {
            return new DuSymlink(path.toAbsolutePath(), children, size);
        }

    }

    private static @NotNull List<DuFile> children(@NotNull Path path, int curDepth) {
        List<DuFile> children = new ArrayList<>();

        try (Stream<Path> childrenStream = Files.list(path)) {
            List<Path> childrenList = childrenStream.toList();

            for (Path childPath : childrenList) {
                DuFile childrenFile = build(childPath, curDepth + 1);
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
            if (!(child instanceof DuSymlink)) {
                size += child.size();
            }
        }

        return size;
    }

    private static void addToVisited(@NotNull Path path) {
        try {
            visited.add(path.toRealPath());
        }
        catch (IOException e) {
            throw new DuIOException(e);
        }

    }

    private static boolean containsVisited(@NotNull Path path){
        try {
            return visited.contains(path.toRealPath());
        }
        catch (IOException e) {
            throw new DuIOException(e);
        }
    }
}