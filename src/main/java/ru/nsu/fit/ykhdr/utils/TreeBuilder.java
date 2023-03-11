package ru.nsu.fit.ykhdr.utils;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.exception.DuIOException;
import ru.nsu.fit.ykhdr.model.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * A utility class that allows you to build a tree based on several parameters and a root directory
 */

public class TreeBuilder {
    private final Set<Path> visited = new HashSet<>();

    /**
     * Builds a new tree containing classes inherited from the DuFile interface from path to root directory.
     * <p>
     *
     * @param root path to root directory for building the tree.
     *             <p>
     * @return {@link DuFile} with fully constructed children.
     * <p>
     * @throws DuIOException if the file contained in the root directory tree was not defined as : directory, regular file, or symlink.
     *                       And also if the user does not have the necessary rights when accessing the directory / regular file / symlink.
     */

    public @NotNull DuFile build(@NotNull Path root) {
        return build(root, 0);
    }

    private @NotNull DuFile build(@NotNull Path curPath, int depth) {
        if (Files.isSymbolicLink(curPath)) {
            return buildSymlink(curPath, depth);
        } else if (Files.isDirectory(curPath)) {
            return buildDirectory(curPath, depth);
        } else if (Files.isRegularFile(curPath)) {
            return buildRegularFile(curPath);
        } else {
            throw new DuIOException("Unknown file is in this directory : " + curPath.toFile().getAbsolutePath());
        }
    }

    private @NotNull DuDirectory buildDirectory(@NotNull Path path, int depth) {
        List<DuFile> children = children(path, depth);
        long size = size(children);

        return new DuDirectory(path, children, size);
    }

    private @NotNull DuRegularFile buildRegularFile(@NotNull Path path) {
        return new DuRegularFile(path, path.toFile().length());
    }

    private @NotNull DuSymlink buildSymlink(@NotNull Path path, int depth) {
        if (!visited.add(toRealPath(path))) {
            return new DuSymlink(path, new ArrayList<>(), 0);
        }

        List<DuFile> children = children(path, depth);
        long size = size(children);

        return new DuSymlink(path, children, size);
    }

    private @NotNull List<DuFile> children(@NotNull Path path, int curDepth) {
        List<DuFile> children = new ArrayList<>();

        try (Stream<Path> childrenStream = streamPath(path)) {
            List<Path> childrenList = childrenStream.toList();

            for (Path childPath : childrenList) {
                DuFile childrenFile = build(childPath, curDepth + 1);
                children.add(childrenFile);
            }
        }

        return children;
    }

    private static @NotNull Stream<Path> streamPath(@NotNull Path path) {
        try {
            return Files.list(path);
        } catch (AccessDeniedException e) {
            throw new DuIOException("Access to directory/file data denied : " + path);
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

    private static @NotNull Path toRealPath(@NotNull Path path) {
        try {
            return path.toRealPath();
        } catch (NoSuchFileException ok) {
            return path;
        } catch (IOException e) {
            throw new DuIOException(e);
        }
    }
}