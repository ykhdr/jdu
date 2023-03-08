package ru.nsu.fit.ykhdr.utils;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.exception.DuIOException;
import ru.nsu.fit.ykhdr.model.DuDirectory;
import ru.nsu.fit.ykhdr.model.DuFile;
import ru.nsu.fit.ykhdr.model.DuRegularFile;
import ru.nsu.fit.ykhdr.model.DuSymlink;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A utility class that allows you to build a tree based on several parameters and a root directory
 */

public class TreeBuilder {

    private static int maxDepth;
    private static boolean followSymlink;


    /**
     * Builds a new tree containing classes inherited from the DuFile interface from path to root directory.
     * <p>
     *
     * @param root path to root directory for building the tree.
     *             <p>
     * @return new DuFile with children fully constructed to the given depth.
     * <p>
     * @throws DuIOException if the file contained in the root directory tree was not defined as : directory, regular file, or symlink.
     *                       And also if the user does not have the necessary rights when accessing the directory / regular file / symlink.
     */

    public static @NotNull DuFile build(@NotNull Path root, int maxDepth, boolean followSymlink) {
        TreeBuilder.maxDepth = maxDepth;
        TreeBuilder.followSymlink = followSymlink;

        return build(root, new HashSet<>(), 0);
    }

    private static @NotNull DuFile build(@NotNull Path curPath, @NotNull Set<Path> visited, int depth) {
        if (Files.isSymbolicLink(curPath)) {
            return buildSymlink(curPath, visited, depth);
        } else if (Files.isDirectory(curPath)) {
            return buildDirectory(curPath, visited, depth);
        } else if (Files.isRegularFile(curPath)) {
            return buildRegularFile(curPath, visited);
        } else {
            throw new DuIOException("Unknown file is in this directory : " + curPath.toFile().getAbsolutePath());
        }
    }

    private static @NotNull DuDirectory buildDirectory(@NotNull Path path, @NotNull Set<Path> visited, int depth) {
        if (containsVisited(path, visited)) {
            return new DuDirectory(path.toAbsolutePath(), null, 0);
        }

        addToVisited(path, visited);

        List<DuFile> children = children(path, visited, depth);
        long size = size(children);

        if (depth >= maxDepth || children.isEmpty()) {
            return new DuDirectory(path.toAbsolutePath(), null, size);
        }

        return new DuDirectory(path.toAbsolutePath(), children, size);
    }

    private static @NotNull DuRegularFile buildRegularFile(@NotNull Path path, @NotNull Set<Path> visited) {
        addToVisited(path, visited);
        return new DuRegularFile(path.toAbsolutePath(), path.toFile().length());
    }

    private static @NotNull DuSymlink buildSymlink(@NotNull Path path, @NotNull Set<Path> visited, int depth) {
        if (containsVisited(path, visited)) {
            return new DuSymlink(path.toAbsolutePath(), null, 0);
        }

        if (Files.isRegularFile(path)) {
            return new DuSymlink(path.toAbsolutePath(), null, path.toFile().length());
        }


        addToVisited(path, visited);
        List<DuFile> children = children(path, visited, depth);
        long size = size(children);

        if (!followSymlink || depth >= maxDepth) {
            return new DuSymlink(path.toAbsolutePath(), null, size);
        } else {
            return new DuSymlink(path.toAbsolutePath(), children, size);
        }
    }

    private static @NotNull List<DuFile> children(@NotNull Path path, @NotNull Set<Path> visited, int curDepth) {
        List<DuFile> children = new ArrayList<>();

        try (Stream<Path> childrenStream = streamPath(path)) {
            List<Path> childrenList = childrenStream.toList();

            for (Path childPath : childrenList) {
                DuFile childrenFile = build(childPath, visited, curDepth + 1);
                children.add(childrenFile);
            }
        }

        return children;
    }

    private static @NotNull Stream<Path> streamPath(@NotNull Path path) {
        try {
            return Files.list(path);
        } catch (AccessDeniedException e) {
            throw new DuIOException("Access to directory/file data denied : " + path, e);
        } catch (FileSystemException e) {
            return Stream.<Path>builder().build();
        } catch (IOException e) {
            throw new DuIOException(e);
        }
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

    private static void addToVisited(@NotNull Path path, @NotNull Set<Path> visited) {
        try {
            visited.add(path.toRealPath());
        } catch (FileSystemException ignored) {

        } catch (IOException e) {
            throw new DuIOException(e);
        }
    }

    private static boolean containsVisited(@NotNull Path path, @NotNull Set<Path> visited) {
        try {
            return visited.contains(path.toRealPath());
        } catch (FileSystemException ignored) {
            return true;
        } catch (IOException e) {
            throw new DuIOException(e);
        }
    }
}