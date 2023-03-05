package ru.nsu.fit.ykhdr.utils;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.exception.DuIOException;
import ru.nsu.fit.ykhdr.model.DuDirectory;
import ru.nsu.fit.ykhdr.model.DuFile;
import ru.nsu.fit.ykhdr.model.DuRegularFile;
import ru.nsu.fit.ykhdr.model.DuSymlink;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A utility class that allows you to build a tree based on several parameters and a root directory
 *
 */

public class TreeBuilder {

    private final int maxDepth;
    private final boolean followSymlink;
    private final Set<Path> visited;

    /**
     * Creates a new TreeBuilder with the given depth parameter and flag for symlink navigation.
     * <p>
     * @param maxDepth
     *        tree building depth.
     * @param followSymlink
     *        flag indicating the need to follow symlinks.
     */
    public TreeBuilder(int maxDepth, boolean followSymlink) {
        this.maxDepth = maxDepth;
        this.followSymlink = followSymlink;
        this.visited = new HashSet<>();
    }

    /**
     * Builds a new tree containing classes inherited from the DuFile interface from path to root directory.
     * <p>
     * @param root
     *        path to root directory for building the tree.
     * <p>
     * @return new DuFile with children fully constructed to the given depth.
     * <p>
     * @throws DuIOException
     *         if the file contained in the root directory tree was not defined as : directory, regular file, or symlink.
     *         And also if the user does not have the necessary rights when accessing the directory / regular file / symlink.
     */

    public @NotNull DuFile build(Path root) {
        return build(root, 0);
    }

    private @NotNull DuFile build(@NotNull Path curPath, int depth) {
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

    private @NotNull DuDirectory buildDirectory(@NotNull Path path, int depth) {
        if (containsVisited(path)) {
            return new DuDirectory(path.toAbsolutePath(), null, 0);
        }

        addToVisited(path);

        List<DuFile> children = children(path, depth);
        long size = size(children);

        if (depth >= maxDepth || children.isEmpty()) {
            return new DuDirectory(path.toAbsolutePath(), null, size);
        }

        return new DuDirectory(path.toAbsolutePath(), children, size);
    }

    private @NotNull DuRegularFile buildRegularFile(@NotNull Path path) {
        addToVisited(path);
        return new DuRegularFile(path.toAbsolutePath(), path.toFile().length());
    }

    private @NotNull DuSymlink buildSymlink(@NotNull Path path, int depth) {
        if (containsVisited(path)) {
            return new DuSymlink(path.toAbsolutePath(), null, 0);
        }

        if (Files.isRegularFile(path)) {
            return new DuSymlink(path.toAbsolutePath(), null, path.toFile().length());
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

    private @NotNull List<DuFile> children(@NotNull Path path, int curDepth) {
        List<DuFile> children = new ArrayList<>();

        try (Stream<Path> childrenStream = Files.list(path)) {
            List<Path> childrenList = childrenStream.toList();

            for (Path childPath : childrenList) {
                DuFile childrenFile = build(childPath, curDepth + 1);
                children.add(childrenFile);
            }
        }
        catch (AccessDeniedException e) {
            throw new DuIOException("Access to directory/file data denied : " + path);
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

    private void addToVisited(@NotNull Path path) {
        try {
            visited.add(path.toRealPath());
        }
        catch (IOException e) {
            throw new DuIOException(e);
        }
    }

    private boolean containsVisited(@NotNull Path path) {
        try {
            return visited.contains(path.toRealPath());
        }
        catch (IOException e) {
            throw new DuIOException(e);
        }
    }
}