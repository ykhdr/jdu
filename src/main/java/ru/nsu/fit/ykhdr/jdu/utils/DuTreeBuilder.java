package ru.nsu.fit.ykhdr.jdu.utils;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.jdu.exception.DuIOException;
import ru.nsu.fit.ykhdr.jdu.model.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * A utility class that allows you to build a tree based on several parameters and a root directory
 */

public class DuTreeBuilder {
    private static final Logger logger = Logger.getLogger(DuTreeBuilder.class.getName());
    private final Set<Path> visited = new HashSet<>();
    private Map<Path, DuFile> treeNodes;

    /**
     * Builds a new tree containing classes inherited from the DuFile interface from path to root directory.
     * <p>
     *
     * @param path {@link Path} to root file for building the tree.
     *             <p>
     * @return {@link Map } with constructed nodes. There is no guarantee that DuSymlink has a target.
     * Use DuLinker to link symlink tags to the symlinks themselves
     * <p>
     * @throws DuIOException if the file contained in the root directory tree was not defined as : directory, regular file, or symlink.
     *                       And also if the user does not have the necessary rights when accessing the directory / regular file / symlink.
     */
    public @NotNull DuFile buildTree(@NotNull Path path, @NotNull Map<Path, DuFile> treeNodes) {
        this.treeNodes = treeNodes;
        return build(path);
    }

    private @NotNull DuFile build(@NotNull Path path) {
        if (Files.isSymbolicLink(path)) {
            return buildSymlink(path);
        } else if (Files.isDirectory(path)) {
            return buildDirectory(toRealPath(path));
        } else if (Files.isRegularFile(path)) {
            return buildRegularFile(toRealPath(path));
        }

        DuUnknownFile unknownFile = new DuUnknownFile(path);
        treeNodes.put(path, unknownFile);
        return unknownFile;
    }

    private @NotNull DuDirectory buildDirectory(@NotNull Path path) {
        List<DuFile> children = children(path);
        long size = size(children);

        DuDirectory directory = new DuDirectory(path, children, size);
        treeNodes.put(path, directory);

        return directory;
    }

    private @NotNull DuRegularFile buildRegularFile(@NotNull Path path) {
        try {
            DuRegularFile regularFile = new DuRegularFile(path, Files.size(path));
            treeNodes.put(path, regularFile);
            return regularFile;
        } catch (IOException e) {
            throw new DuIOException(e);
        }
    }

    private @NotNull DuSymlink buildSymlink(@NotNull Path path) {
        Path targetPath = toRealPath(path);

        DuSymlink symlink;

        if (!visited.add(targetPath)) {
            symlink = new DuSymlink(path, targetPath, new DuUnknownFile(targetPath), 0);
        } else {
            DuFile target = build(targetPath);
            symlink = new DuSymlink(path, targetPath, target, target.size());
        }

        treeNodes.put(path, symlink);

        return symlink;
    }

    private @NotNull List<DuFile> children(@NotNull Path path) {
        try (Stream<Path> childrenStream = streamPath(path)) {
            return childrenStream.map(this::build).toList();
        }
    }

    private static @NotNull Stream<Path> streamPath(@NotNull Path path) {
        try {
            return Files.list(path);
        } catch (AccessDeniedException e) {
            logger.log(Level.WARNING, "Access to directory/file data denied : " + path);
            return Stream.<Path>builder().build();
        } catch (FileSystemException e) {
            logger.log(Level.WARNING, "File system operation denied : " + path);
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
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage() + ": " + path);
            return path.toAbsolutePath();
        }
    }
}