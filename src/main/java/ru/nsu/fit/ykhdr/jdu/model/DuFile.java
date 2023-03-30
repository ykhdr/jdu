package ru.nsu.fit.ykhdr.jdu.model;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * The DuFile interface represents a file or directory in the file system.
 * <p/>
 * This interface is part of a sealed hierarchy of classes that represent different types of files,
 * including regular files, directories, symlinks, and unknown files. Classes that implement this interface must be one of these types.
 */
public sealed interface DuFile permits DuDirectory, DuRegularFile, DuSymlink, DuUnknownFile {
    /**
     * Returns the size of the file in bytes.
     * <p/>
     * This method returns the size of the file represented by this object in bytes.
     * If this object represents a directory, the size of the directory is calculated recursively by summing
     * the sizes of all the files and subdirectories it contains.
     *
     * @return the size of the file in bytes
     */
    long size();

    /**
     * Returns the short filename of the file or directory.
     * <p/>
     * This method returns the short filename of the file or directory represented by this object.
     * If the filename cannot be determined for any reason, this method returns "{Unknown name}".
     *
     * @return the short filename of the file or directory
     */
    @NotNull default String name() {
        Path fileName = path().getFileName();
        return fileName == null ? "{Unknown name}" : fileName.toString();
    }

    /**
     * Returns the {@link Path} of the file or directory.
     * <p/>
     * This method returns the {@link Path} object that represents the file or directory on the file system.
     *
     * @return the {@link Path} of the file or directory
     */
    @NotNull Path path();
}