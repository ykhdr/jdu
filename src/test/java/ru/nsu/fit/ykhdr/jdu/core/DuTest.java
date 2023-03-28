package ru.nsu.fit.ykhdr.jdu.core;

import org.junit.Rule;

import java.nio.file.FileSystem;

public class DuTest {
    @Rule
    public final FileSystemRule fileSystemRule = new FileSystemRule();

    protected FileSystem fileSystem() {
        return fileSystemRule.getFileSystem();
    }

}
