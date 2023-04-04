package ru.nsu.fit.ykhdr.jdu.model;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Represents a symbolic link in the file system.
 */
public final class DuSymlink implements DuFile {
    private final @NotNull Path path;
    private final @NotNull Path targetPath;
    private @NotNull DuFile target;
    private final long size;

    /**
     * Constructs a new {@code DuSymlink} instance with the given parameters.
     *
     * @param path       the path to the symbolic link.
     * @param targetPath the path to the target of the symbolic link.
     * @param target     the target of the symbolic link.
     * @param size       the size of the file pointed to by the symbolic link.
     */
    // CR: just pass target
    public DuSymlink(@NotNull Path path, @NotNull Path targetPath, @NotNull DuFile target, long size) {
        this.path = path;
        this.targetPath = targetPath;
        this.target = target;
        this.size = size;
    }

    /**
     * Gets the target file of the symbolic link.
     *
     * @return the target file of the symbolic link.
     */
    public @NotNull DuFile getTarget() {
        return target;
    }

    /**
     * Sets the target file of the symbolic link.
     *
     * @param target the target file of the symbolic link.
     */
    public void setTarget(@NotNull DuFile target) {
        this.target = target;
    }

    /**
     * Gets the path to the target of the symbolic link.
     *
     * @return the path to the target of the symbolic link.
     */
    public @NotNull Path getTargetPath() {
        return targetPath;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public @NotNull Path path() {
        return path;
    }

    @Override
    public String toString() {
        return "DuSymlink{" +
                "path=" + path +
                ", targetPath=" + targetPath +
                ", target=" + target +
                ", size=" + size +
                '}';
    }
}