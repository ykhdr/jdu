package ru.nsu.fit.ykhr.jdu;

import junit.framework.TestCase;
import org.junit.Test;
import ru.nsu.fit.ykhdr.jdu.model.DuFile;
import ru.nsu.fit.ykhdr.jdu.utils.DuTreeBuilder;
import ru.nsu.fit.ykhr.jdu.core.DuTest;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.nsu.fit.ykhr.jdu.core.DuTreeElement.*;


public class DuTreeBuilderTest extends DuTest {

    /*

    1. root is file
    2. root is symlink
    3. root is directory, no children
    4. root is directory, one child (regular)
    5. root is directory, one child (directory)
    6. root is directory, one child (symlink)
    7. root is symlink, resolves to directory
    8. root is symlink, resolves to regular
    9. root is symlink, resolves to symlink
    10. cycles:
        - symlink references itself?
        - symlink to symlink to symlink
        - symlink to parent dir
     */

    @Test
    public void testOneFileInDirectory() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("/foo");
        Files.createDirectory(fooPath);
        Path barPath = fooPath.resolve("bar.txt");
        Files.createFile(barPath);
        DuTreeBuilder treeBuilder = new DuTreeBuilder();

        DuFile actual = treeBuilder.build(fooPath);
        DuFile expected = tree(fs, dir("foo", file("bar.txt")));
        TestCase.assertEquals(expected, actual);
    }

    /*
    * structure:
    *   /foo
    * */
    @Test
    public void testOneDirectory() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("/foo");
        Files.createDirectory(fooPath);
        DuTreeBuilder treeBuilder = new DuTreeBuilder();

        DuFile actual = treeBuilder.build(fooPath);
        DuFile expected = tree(fs, dir("foo"));
        TestCase.assertEquals(expected, actual);
    }

    /*
     * structure:
     *  /foo
     *    bar.txt
     * */
    @Test
    public void testOneFile() throws IOException {
        FileSystem fs = fileSystem();
        Path dirPath = fs.getPath("/foo");
        Files.createDirectory(dirPath);
        Path filePath = dirPath.resolve("bar.txt");
        Files.createFile(filePath);
        DuTreeBuilder treeBuilder = new DuTreeBuilder();

        DuFile actual = treeBuilder.build(filePath);
        DuFile expected = tree(fs, file("/foo/bar.txt"));
        TestCase.assertEquals(expected, actual);
    }


    /*
    * structure:
    *  /foo
    *    /bar
    *    /baz
    * */
    @Test
    public void testTwoDirectoriesInDirectory() throws IOException {
        FileSystem fs = fileSystem();
        Path rootDirPath = fs.getPath("/foo");
        Files.createDirectory(rootDirPath);
        Path firstDirPath = rootDirPath.resolve("bar");
        Files.createDirectory(firstDirPath);
        Path secondDirPath = rootDirPath.resolve("baz");
        Files.createDirectory(secondDirPath);
        DuTreeBuilder treeBuilder = new DuTreeBuilder();

        DuFile actual = treeBuilder.build(rootDirPath);
        DuFile expected = tree(fs, dir("/foo", dir("/foo/bar"), dir("/foo/baz")));
        TestCase.assertEquals(expected, actual);
    }

    /*
     *  structure:
     *   /foo
     *     bar.txt
     *     link@ -> /foo/bar.txt
     * */
    @Test
    public void testSymlinkTargetToFile() throws IOException {

        FileSystem fs = fileSystem();
        Path dirPath = fs.getPath("/foo");
        Files.createDirectory(dirPath);
        Path filePath = dirPath.resolve("bar.txt");
        Files.createFile(filePath);
        Path symlinkPath = dirPath.resolve("link");
        Files.createSymbolicLink(symlinkPath, filePath);
        DuTreeBuilder treeBuilder = new DuTreeBuilder();

        DuFile actual = treeBuilder.build(dirPath);
        DuFile expected = tree(fs, dir("/foo", file("/foo/bar.txt"), link("/foo/link")));
        TestCase.assertEquals(expected, actual);
    }

    /*
     * structure:
     *   /foo
     *     /bar
     *     link@ -> /foo/bar
     * */
    @Test
    public void testSymlinkTargetToDirectory() throws IOException {

        FileSystem fs = fileSystem();
        Path rootPath = fs.getPath("/foo");
        Files.createDirectory(rootPath);
        Path dirPath = rootPath.resolve("bar");
        Files.createDirectory(dirPath);
        Path symlinkPath = rootPath.resolve("link");
        Files.createSymbolicLink(symlinkPath, dirPath);
        DuTreeBuilder treeBuilder = new DuTreeBuilder();

        DuFile actual = treeBuilder.build(rootPath);
        DuFile expected = tree(fs, dir("/foo", dir("/foo/bar"), link("/foo/link")));
        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testSymlinkTargetToDirectoryWithFile() throws IOException {
        /*
        *
        *
        * */

        FileSystem fs = fileSystem();
        Path rootPath = fs.getPath("/foo");
        Files.createDirectory(rootPath);
        Path dirPath = rootPath.resolve("bar");
        Files.createDirectory(dirPath);
        Path filePath = dirPath.resolve("baz.png");
        Files.createFile(filePath);
        Path symlinkPath = rootPath.resolve("link");
        Files.createSymbolicLink(symlinkPath, dirPath);
        DuTreeBuilder treeBuilder = new DuTreeBuilder();

        DuFile actual = treeBuilder.build(rootPath);
        DuFile expected = tree(fs, dir("/foo",
                dir("/foo/bar", file("/foo/bar/baz.png")),
                link("/foo/link", file("/foo/link/baz.png"))));
        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void testSymlinkCycle() throws IOException {
        FileSystem fs = fileSystem();
        Path rootPath = fs.getPath("/foo");
        Files.createDirectory(rootPath);
        Path dir1Path = rootPath.resolve("bar");
        Files.createDirectory(dir1Path);
        Path dir2Path = rootPath.resolve("baz");
        Files.createDirectory(dir2Path);

        Path link1Path = dir1Path.resolve("link1");
        Files.createSymbolicLink(link1Path, dir2Path);
        Path link2Path = dir2Path.resolve("link2");
        Files.createSymbolicLink(link2Path, dir1Path);
        DuTreeBuilder treeBuilder = new DuTreeBuilder();

        DuFile actual = treeBuilder.build(rootPath);
        DuFile expected = tree(fs, dir("/foo",
                dir("/foo/bar", link("/foo/bar/link1", link("/foo/bar/link1/link2",link("/foo/bar/link1/link2/link1")))),
                dir("/foo/baz", link("/foo/baz/link2"))));
        TestCase.assertEquals(expected, actual);
    }
}
