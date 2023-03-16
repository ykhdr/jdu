package ru.nsu.fit.ykhdr.jdu.utils;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.jdu.exception.DuIOException;
import ru.nsu.fit.ykhdr.jdu.model.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

/**
 * A utility class that allows you to build a tree based on several parameters and a root directory
 */

public class DuTreeBuilder {

    private final Set<Path> visited = new HashSet<>();

    /**
     * Builds a new tree containing classes inherited from the DuFile interface from path to root directory.
     * <p>
     *
     * @param path path to root directory for building the tree.
     *             <p>
     * @return {@link DuFile} with fully constructed children.
     * <p>
     * @throws DuIOException if the file contained in the root directory tree was not defined as : directory, regular file, or symlink.
     *                       And also if the user does not have the necessary rights when accessing the directory / regular file / symlink.
     */
    @NotNull
    public DuFile build(@NotNull Path path) {
        if (Files.isSymbolicLink(path)) {
            return buildSymlink(path);
        } else if (Files.isDirectory(path)) {
            return buildDirectory(path);
        } else if (Files.isRegularFile(path)) {
            return buildRegularFile(path);
        } else {
            /*
            CR: choose one option:
            1. stop building tree
            2. warn user, do not add a node (logger, slf4j, log4j...)
            3. warn user, add a node and hope that size works (if not then 2)
             */
            throw new DuIOException("Unknown file type for file " + path.toFile().getAbsolutePath());
        }
    }

    private @NotNull DuDirectory buildDirectory(@NotNull Path path) {
        List<DuFile> children = children(path);
        long size = size(children);

        return new DuDirectory(path, children, size);
    }

    private @NotNull DuRegularFile buildRegularFile(@NotNull Path path) {
        try {
            return new DuRegularFile(path, Files.size(path));
        }
        catch (IOException e) {
            throw new DuIOException(e);
        }
    }

    private @NotNull DuSymlink buildSymlink(@NotNull Path path) {
        // CR: toRealPath as field, use in printer
        if (!visited.add(toRealPath(path))) {
            return new DuSymlink(path, new ArrayList<>(), 0);
        }

        List<DuFile> children = children(path);
        long size = size(children);

        return new DuSymlink(path, children, size);
    }

    private @NotNull List<DuFile> children(@NotNull Path path) {
        List<DuFile> children = new ArrayList<>();

        try (Stream<Path> childrenStream = streamPath(path)) {
            // CR: stream
            List<Path> childrenList = childrenStream.toList();

            for (Path childPath : childrenList) {
                DuFile childrenFile = build(childPath);
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
            // CR: merge with NoSuchFileException case
            // CR: log
            throw new DuIOException(e);
        }
    }
}