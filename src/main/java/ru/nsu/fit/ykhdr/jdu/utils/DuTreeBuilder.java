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
 * The DuTreeBuilder class is responsible for building a tree of {@link DuFile} objects from a given {@link Path}.
 * <p/>
 * This class provides a method, {@link #buildTree(Path)}, which takes a {@link Path} object representing a file
 * or directory and recursively builds a tree of {@link DuFile} objects.
 * The resulting {@link DuFile} object at the root of the tree represents the file or directory specified by the input {@link Path}.
 */
public class DuTreeBuilder {

    private static final Logger LOG = Logger.getLogger(DuTreeBuilder.class.getName());

    private final Set<Path> visited = new HashSet<>();
    private final List<DuSymlink> symlinksWithoutTarget = new ArrayList<>();
    private final Map<Path, DuFile> treeNodes = new HashMap<>();

    /**
     * Builds a tree of {@link DuFile} objects from the given {@link Path}.
     * <p/>
     * This method takes a {@link Path} object representing a file or directory and recursively builds a tree of {@link DuFile} objects.
     * The resulting {@link DuFile} object at the root of the tree represents the file or directory specified by the input {@link Path}.
     *
     * @param path the {@link Path} object representing the file or directory to build the tree from
     * @return the root {@link DuFile} object representing the specified file or directory
     * @throws DuIOException if the file in a tree does not exist or an I/O error occurs
     */
    public @NotNull DuFile buildTree(@NotNull Path path) {
        DuFile root = build(path);
        linkSymlinks();

        return root;
    }

    private @NotNull DuFile build(@NotNull Path path) {
        if (Files.isSymbolicLink(path)) {
            return buildSymlink(path);
        } else if (Files.isDirectory(path)) {
            return buildDirectory(path);
        } else if (Files.isRegularFile(path)) {
            return buildRegularFile(path);
        }

        return new DuUnknownFile(path, fileSize(path));
    }

    private @NotNull DuDirectory buildDirectory(@NotNull Path path) {
        List<DuFile> children = children(path);
        long size = childSize(children);

        return new DuDirectory(path, children, size);
    }

    private @NotNull DuRegularFile buildRegularFile(@NotNull Path path) {
        return new DuRegularFile(path, fileSize(path));
    }

    private @NotNull DuSymlink buildSymlink(@NotNull Path path) {
        Path targetPath = readSymlink(path);

        DuSymlink symlink;

        if (!visited.add(targetPath)) {
            symlink = new DuSymlink(path, targetPath, new DuUnknownFile(targetPath, fileSize(targetPath)), fileSize(path));
            symlinksWithoutTarget.add(symlink);
        } else {
            DuFile target = build(targetPath);
            treeNodes.put(targetPath, target);
            symlink = new DuSymlink(path, targetPath, target, target.size());
        }

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
            LOG.log(Level.WARNING, "Access to directory/file data denied : " + path);
            return Stream.<Path>builder().build();
        } catch (FileSystemException e) {
            LOG.log(Level.WARNING, "File system operation denied : " + path);
            return Stream.<Path>builder().build();
        } catch (IOException e) {
            throw new DuIOException(e);
        }
    }

    private static long childSize(@NotNull List<DuFile> children) {
        long size = 0;

        for (DuFile child : children) {
            if (!(child instanceof DuSymlink)) {
                size += child.size();
            }
        }

        return size;
    }

    private static long fileSize(@NotNull Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "No such file or other IO exception :" + path);
            return 0;
        }
    }

    private static @NotNull Path readSymlink(@NotNull Path link) {
        try {
            return Files.readSymbolicLink(link);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Bad reading a symlink target :" + link);
            return toRealPath(link);
        }
    }

    private static @NotNull Path toRealPath(@NotNull Path path) {
        try {
            return path.toRealPath();
        } catch (IOException e) {
            LOG.log(Level.WARNING, "No such file or other IO exception :" + path);
            return path.toAbsolutePath();
        }
    }

    private void linkSymlinks() {
        for (DuSymlink symlink : symlinksWithoutTarget) {
            symlink.setTarget(treeNodes.get(symlink.getTargetPath()));
        }
    }
}