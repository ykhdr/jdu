package ru.nsu.fit.ykhr.jdu;

import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.ykhdr.jdu.model.DuFile;
import ru.nsu.fit.ykhdr.jdu.utils.DuTreeBuilder;
import ru.nsu.fit.ykhdr.jdu.utils.DuTreePrinter;
import ru.nsu.fit.ykhr.jdu.core.DuTest;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class DuTreePrinterTest extends DuTest {
    @Test
    public void printFileInDirectoryTest() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("/foo");
        Files.createDirectory(fooPath);
        Path barPath = fooPath.resolve("bar.txt");
        Files.createFile(barPath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream pos = new PrintStream(baos)) {
            DuTreePrinter printer = new DuTreePrinter(pos, 10, 5, false);

            DuTreeBuilder builder = new DuTreeBuilder();
            DuFile duFile = builder.build(fooPath);

            printer.print(duFile);
        }

        Assert.assertArrayEquals("""
                /foo [0.0 B]
                  bar.txt [0.0 B]
                """.getBytes(), baos.toByteArray());
    }

    @Test
    public void printDirectoryTest() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("/foo");
        Files.createDirectory(fooPath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream pos = new PrintStream(baos)) {
            DuTreePrinter printer = new DuTreePrinter(pos, 10, 5, false);

            DuTreeBuilder builder = new DuTreeBuilder();
            DuFile duFile = builder.build(fooPath);

            printer.print(duFile);
        }

        Assert.assertArrayEquals("""
                /foo [0.0 B]
                """.getBytes(), baos.toByteArray());
    }

    @Test
    public void printTwoDirectoriesWithFilesTest() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("/foo");
        Files.createDirectory(fooPath);
        Path dir1Path = fooPath.resolve("dir1");
        Files.createDirectory(dir1Path);
        Path file1Path = dir1Path.resolve("file1");
        Files.createFile(file1Path);
        Path dir2Path = fooPath.resolve("dir2");
        Files.createDirectory(dir2Path);
        Path file2Path = dir2Path.resolve("file2");
        Files.createFile(file2Path);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream pos = new PrintStream(baos)) {
            DuTreePrinter printer = new DuTreePrinter(pos, 10, 5, false);

            DuTreeBuilder builder = new DuTreeBuilder();
            DuFile duFile = builder.build(fooPath);

            printer.print(duFile);
        }

        Assert.assertArrayEquals("""
                /foo [0.0 B]
                  /dir1 [0.0 B]
                    file1 [0.0 B]
                  /dir2 [0.0 B]
                    file2 [0.0 B]
                """.getBytes(), baos.toByteArray());
    }

    @Test
    public void printSymlinkTargetToFileInDifferentDirectoriesTest() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("/foo");
        Files.createDirectory(fooPath);
        Path dir1Path = fooPath.resolve("dir1");
        Files.createDirectory(dir1Path);
        Path filePath = dir1Path.resolve("file");
        Files.createFile(filePath);
        Path dir2Path = fooPath.resolve("dir2");
        Files.createDirectory(dir2Path);
        Path linkPath = dir2Path.resolve("link");
        Files.createSymbolicLink(linkPath, filePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream pos = new PrintStream(baos)) {
            DuTreePrinter printer = new DuTreePrinter(pos, 10, 5, false);

            DuTreeBuilder builder = new DuTreeBuilder();
            DuFile duFile = builder.build(fooPath);

            printer.print(duFile);
        }

        Assert.assertArrayEquals("""
                /foo [0.0 B]
                  /dir1 [0.0 B]
                    file [0.0 B]
                  /dir2 [0.0 B]
                    link@ -> /foo/dir1/file [0.0 B]
                """.getBytes(), baos.toByteArray());
    }

    @Test
    public void printSymlinkCycleWithoutFollowSymlinksOptionTest() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("/foo");
        Files.createDirectory(fooPath);
        Path dir1Path = fooPath.resolve("dir1");
        Files.createDirectory(dir1Path);
        Path dir2Path = fooPath.resolve("dir2");
        Files.createDirectory(dir2Path);
        Path link1Path = dir1Path.resolve("link1");
        Files.createSymbolicLink(link1Path, dir2Path);
        Path link2Path = dir2Path.resolve("link2");
        Files.createSymbolicLink(link2Path, dir1Path);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream pos = new PrintStream(baos)) {
            DuTreePrinter printer = new DuTreePrinter(pos, 10, 5, false);

            DuTreeBuilder builder = new DuTreeBuilder();
            DuFile duFile = builder.build(fooPath);

            printer.print(duFile);
        }

        Assert.assertArrayEquals("""
                /foo [0.0 B]
                  /dir1 [0.0 B]
                    link1@ -> /foo/dir2 [0.0 B]
                  /dir2 [0.0 B]
                    link2@ -> /foo/dir1 [0.0 B]
                """.getBytes(), baos.toByteArray());
    }

    @Test
    public void printSymlinkCycleWithFollowSymlinksOptionTest() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("/foo");
        Files.createDirectory(fooPath);
        Path dir1Path = fooPath.resolve("dir1");
        Files.createDirectory(dir1Path);
        Path dir2Path = fooPath.resolve("dir2");
        Files.createDirectory(dir2Path);
        Path link1Path = dir1Path.resolve("link1");
        Files.createSymbolicLink(link1Path, dir2Path);
        Path link2Path = dir2Path.resolve("link2");
        Files.createSymbolicLink(link2Path, dir1Path);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream pos = new PrintStream(baos)) {
            DuTreePrinter printer = new DuTreePrinter(pos, 10, 5, true);

            DuTreeBuilder builder = new DuTreeBuilder();
            DuFile duFile = builder.build(fooPath);

            printer.print(duFile);
        }

        Assert.assertArrayEquals("""
                /foo [0.0 B]
                  /dir1 [0.0 B]
                    link1@ -> /foo/dir2 [0.0 B]
                      link2@ -> /foo/dir1 [0.0 B]
                        link1@ -> /foo/dir2 [0.0 B]
                  /dir2 [0.0 B]
                    link2@ -> /foo/dir1 [0.0 B]
                """.getBytes(), baos.toByteArray());
    }

    @Test
    public void optionDepthTest() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("/foo");
        Files.createDirectory(fooPath);
        Path dir1Path = fooPath.resolve("dir1");
        Files.createDirectory(dir1Path);
        Path file1Path = dir1Path.resolve("file1");
        Files.createFile(file1Path);
        Path barPath = dir1Path.resolve("bar");
        Files.createDirectory(barPath);
        Path bazPath = barPath.resolve("baz");
        Files.createDirectory(bazPath);
        Path dir2Path = fooPath.resolve("dir2");
        Files.createDirectory(dir2Path);
        Path file2Path = dir2Path.resolve("file2");
        Files.createFile(file2Path);

        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        try (PrintStream pos = new PrintStream(baos1)) {
            DuTreePrinter printer1 = new DuTreePrinter(pos, 10, 1, false);

            DuTreeBuilder builder = new DuTreeBuilder();
            DuFile duFile = builder.build(fooPath);

            printer1.print(duFile);
        }

        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        try (PrintStream pos = new PrintStream(baos2)) {
            DuTreePrinter printer = new DuTreePrinter(pos, 10, 2, false);

            DuTreeBuilder builder = new DuTreeBuilder();
            DuFile duFile = builder.build(fooPath);

            printer.print(duFile);
        }

        ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
        try (PrintStream pos = new PrintStream(baos3)) {
            DuTreePrinter printer = new DuTreePrinter(pos, 10, 3, false);

            DuTreeBuilder builder = new DuTreeBuilder();
            DuFile duFile = builder.build(fooPath);

            printer.print(duFile);
        }


        Assert.assertArrayEquals("""
                /foo [0.0 B]
                  /dir1 [0.0 B]
                  /dir2 [0.0 B]
                """.getBytes(), baos1.toByteArray());
        Assert.assertArrayEquals("""
                /foo [0.0 B]
                  /dir1 [0.0 B]
                    /bar [0.0 B]
                    file1 [0.0 B]
                  /dir2 [0.0 B]
                    file2 [0.0 B]
                """.getBytes(), baos2.toByteArray());
        Assert.assertArrayEquals("""
                /foo [0.0 B]
                  /dir1 [0.0 B]
                    /bar [0.0 B]
                      /baz [0.0 B]
                    file1 [0.0 B]
                  /dir2 [0.0 B]
                    file2 [0.0 B]
                """.getBytes(), baos3.toByteArray());
    }

    @Test
    public void limitOptionTest() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("/foo");
        Files.createDirectory(fooPath);
        Path dir1Path = fooPath.resolve("dir1");
        Files.createDirectory(dir1Path);
        Path file1Path = dir1Path.resolve("file1");
        Files.createFile(file1Path);
        Path barPath = dir1Path.resolve("bar");
        Files.createDirectory(barPath);
        Path bazPath = barPath.resolve("baz");
        Files.createDirectory(bazPath);
        Path dir2Path = fooPath.resolve("dir2");
        Files.createDirectory(dir2Path);
        Path file2Path = dir2Path.resolve("file2");
        Files.createFile(file2Path);
        Path dir3Path = fooPath.resolve("dir3");
        Files.createDirectory(dir3Path);

        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        try (PrintStream pos = new PrintStream(baos1)) {
            DuTreePrinter printer1 = new DuTreePrinter(pos, 1, 3, false);

            DuTreeBuilder builder = new DuTreeBuilder();
            DuFile duFile = builder.build(fooPath);

            printer1.print(duFile);
        }

        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        try (PrintStream pos = new PrintStream(baos2)) {
            DuTreePrinter printer = new DuTreePrinter(pos, 2, 3, false);

            DuTreeBuilder builder = new DuTreeBuilder();
            DuFile duFile = builder.build(fooPath);

            printer.print(duFile);
        }

        ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
        try (PrintStream pos = new PrintStream(baos3)) {
            DuTreePrinter printer = new DuTreePrinter(pos, 3, 3, false);

            DuTreeBuilder builder = new DuTreeBuilder();
            DuFile duFile = builder.build(fooPath);

            printer.print(duFile);
        }

        Assert.assertArrayEquals("""
                /foo [0.0 B]
                  /dir1 [0.0 B]
                    /bar [0.0 B]
                      /baz [0.0 B]
                """.getBytes(), baos1.toByteArray());
        Assert.assertArrayEquals("""
                /foo [0.0 B]
                  /dir1 [0.0 B]
                    /bar [0.0 B]
                      /baz [0.0 B]
                    file1 [0.0 B]
                  /dir2 [0.0 B]
                    file2 [0.0 B]
                """.getBytes(), baos2.toByteArray());
        Assert.assertArrayEquals("""
                /foo [0.0 B]
                  /dir1 [0.0 B]
                    /bar [0.0 B]
                      /baz [0.0 B]
                    file1 [0.0 B]
                  /dir2 [0.0 B]
                    file2 [0.0 B]
                  /dir3 [0.0 B]
                """.getBytes(), baos3.toByteArray());
    }
}