package ru.nsu.fit.ykhdr.jdu.model;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Record that implements the DuFile interface and describes the symbolic link structure.
 * <p>
 */
public final class DuSymlink implements DuFile {
    private final @NotNull Path path;
    private final @NotNull Path targetPath;
    private @NotNull DuFile target;
    private final long size;

    /**
     * @param path       path to symlink.
     * @param targetPath path to target of link.
     * @param target     target of link.
     * @param size       the size of the file pointed to by the symlink.
     */
    public DuSymlink(@NotNull Path path, @NotNull Path targetPath, @NotNull DuFile target, long size) {
        this.path = path;
        this.targetPath = targetPath;
        this.target = target;
        this.size = size;
    }

    public @NotNull DuFile getTarget() {
        return target;
    }

    public void setTarget(@NotNull DuFile target) {
        this.target = target;
    }

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