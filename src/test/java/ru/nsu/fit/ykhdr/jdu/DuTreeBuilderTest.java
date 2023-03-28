package ru.nsu.fit.ykhdr.jdu;

import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.ykhdr.jdu.core.DuTreeElement;
import ru.nsu.fit.ykhdr.jdu.model.DuDirectory;
import ru.nsu.fit.ykhdr.jdu.model.DuFile;
import ru.nsu.fit.ykhdr.jdu.model.DuSymlink;
import ru.nsu.fit.ykhdr.jdu.utils.DuTreeBuilder;
import ru.nsu.fit.ykhdr.jdu.core.DuTest;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static ru.nsu.fit.ykhdr.jdu.core.DuTreeElement.*;


public class DuTreeBuilderTest extends DuTest {

    /*
     * structure:
     *   /foo.txt
     */
    @Test
    public void testRootIsRegularFile() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("/foo.txt");
        Files.createFile(fooPath);

        DuTreeBuilder treeBuilder = new DuTreeBuilder();
        DuFile actual = treeBuilder.buildTree(fooPath, new HashMap<>());
        DuFile expected = tree(fs, file("foo.txt"));

        Assert.assertEquals(expected, actual);
    }

    /*
     * structure:
     *   /link -> file.txt
     *   file.txt
     */
    @Test
    public void testRootIsSymlinkResolvesToRegularFile() throws IOException {
        FileSystem fs = fileSystem();
        Path linkPath = fs.getPath("/link");
        Path filePath = fs.getPath("/file.txt");

        Files.createFile(filePath);
        Files.createSymbolicLink(linkPath, filePath);

        DuTreeBuilder treeBuilder = new DuTreeBuilder();
        DuFile actual = treeBuilder.buildTree(linkPath, new HashMap<>());
        DuFile expected = tree(fs, link("/link", file("/file.txt")));

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    /*
     * structure:
     *   /link -> dir
     *   dir
     */
    @Test
    public void testRootIsSymlinkResolvesToDirectory() throws IOException {
        FileSystem fs = fileSystem();
        Path linkPath = fs.getPath("/link");
        Path dirPath = fs.getPath("/foo");
        Files.createDirectory(dirPath);
        Files.createSymbolicLink(linkPath, dirPath);
        DuTreeBuilder treeBuilder = new DuTreeBuilder();
        DuFile actual = treeBuilder.buildTree(linkPath, new HashMap<>());
        DuFile expected = tree(fs, link("/link", dir("/foo")));
        Assert.assertEquals(expected.toString(), actual.toString());
    }

    /*
     * structure:
     *   /foo
     */
    @Test
    public void testRootIsEmptyDirectory() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("/foo");
        Files.createDirectory(fooPath);
        DuTreeBuilder treeBuilder = new DuTreeBuilder();

        DuFile actual = treeBuilder.buildTree(fooPath, new HashMap<>());
        DuFile expected = tree(fs, dir("foo"));
        Assert.assertEquals(expected, actual);
    }

    /*
     * structure:
     *  /foo
     *    bar.txt
     */
    @Test
    public void testRootIsDirectoryWithOneFile() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("/foo");
        Files.createDirectory(fooPath);
        Path barPath = fooPath.resolve("bar.txt");
        Files.createFile(barPath);
        DuTreeBuilder treeBuilder = new DuTreeBuilder();

        DuFile actual = treeBuilder.buildTree(fooPath, new HashMap<>());
        DuFile expected = tree(fs, dir("foo", file("bar.txt")));

        Assert.assertEquals(expected, actual);
    }

    /*
     * structure:
     *  /foo
     *    /bar
     */
    @Test
    public void testRootIsDirectoryWithOneDirectory() throws IOException {
        FileSystem fs = fileSystem();
        Path rootDirPath = fs.getPath("/foo");
        Files.createDirectory(rootDirPath);
        Path firstDirPath = rootDirPath.resolve("bar");
        Files.createDirectory(firstDirPath);
        DuTreeBuilder treeBuilder = new DuTreeBuilder();

        DuFile actual = treeBuilder.buildTree(rootDirPath, new HashMap<>());
        DuFile expected = tree(fs, dir("/foo", dir("/foo/bar")));

        Assert.assertEquals(expected, actual);
    }

    /*
     * structure:
     *  /foo
     *    /bar
     *    /baz
     */
    @Test
    public void testRootIsDirectoryWithTwoDirectories() throws IOException {
        FileSystem fs = fileSystem();
        Path rootDirPath = fs.getPath("/foo");
        Files.createDirectory(rootDirPath);
        Path firstDirPath = rootDirPath.resolve("bar");
        Files.createDirectory(firstDirPath);
        Path secondDirPath = rootDirPath.resolve("baz");
        Files.createDirectory(secondDirPath);
        DuTreeBuilder treeBuilder = new DuTreeBuilder();

        DuFile actual = treeBuilder.buildTree(rootDirPath, new HashMap<>());
        DuFile expected = tree(fs, dir("/foo", dir("/foo/bar"), dir("/foo/baz")));

        Assert.assertEquals(expected, actual);
    }

    /*
     *  structure:
     *   /foo
     *     bar.txt
     *     link@ -> /foo/bar.txt
     */
    @Test
    public void testRootIsDirectoryWithSymlinkResolvesToFile() throws IOException {
        FileSystem fs = fileSystem();
        Path dirPath = fs.getPath("/foo");
        Files.createDirectory(dirPath);
        Path filePath = dirPath.resolve("bar.txt");
        Files.createFile(filePath);
        Path symlinkPath = dirPath.resolve("link");
        Files.createSymbolicLink(symlinkPath, filePath);
        DuTreeBuilder treeBuilder = new DuTreeBuilder();

        DuFile actual = treeBuilder.buildTree(dirPath, new HashMap<>());

        DuTreeElement file = file("/foo/bar.txt");
        DuFile expected = tree(fs, dir("/foo", file, link("/foo/link", file)));

        Assert.assertEquals(expected.toString(), actual.toString());
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
        DuFile actual = treeBuilder.buildTree(rootPath, new HashMap<>());

        DuTreeElement dir = dir("/foo/bar");
        DuFile expected = tree(fs, dir("/foo", dir, link("/foo/link", dir)));

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    /*
     * structure:
     *   /foo
     *     /bar
     *       /baz.png
     *     link -> ../bar
     */
    @Test
    public void testSymlinkTargetToDirectoryWithFile() throws IOException {
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

        DuFile actual = treeBuilder.buildTree(rootPath, new HashMap<>());

        DuTreeElement file = file("/foo/bar/baz.png");
        DuTreeElement dir = dir("/foo/bar", file);

        DuFile expected = tree(fs, dir("/foo", dir, link("/foo/link", dir)));

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    /*
     * structure:
     *   link1 -> link2
     *   link2 -> link1
     */
    @Test
    public void testSymlinksTargetToEachOthers() throws IOException {
        FileSystem fs = fileSystem();
        Path link1Path = fs.getPath("/link1");
        Path link2Path = fs.getPath("/link2");
        Files.createSymbolicLink(link1Path, link2Path);
        Files.createSymbolicLink(link2Path, link1Path);

        DuTreeBuilder treeBuilder = new DuTreeBuilder();

        // thrown IO exception: too many levels of symbolic links.
        // Returns absolute path -> link1Path
        DuSymlink actual = (DuSymlink) treeBuilder.buildTree(link1Path, new HashMap<>());

        DuTreeElement linkTarget = link("/link1");
        DuTreeElement link = link("/link1", linkTarget);
        linkTarget.children().add(link);

        DuSymlink expected = (DuSymlink) tree(fs, linkTarget);

        Assert.assertEquals(expected.path().toString(), actual.path().toString());
        Assert.assertEquals(expected.getTarget().path().toString(), actual.getTarget().path().toString());
    }

    /*
     * structure:
     *   /foo
     *     link -> ../foo
     */
    @Test
    public void testSymlinkTargetToParentDirectory() throws IOException {
        FileSystem fs = fileSystem();
        Path rootPath = fs.getPath("/foo");
        Files.createDirectory(rootPath);
        Path linkPath = rootPath.resolve("link");
        Files.createSymbolicLink(linkPath, rootPath);

        DuTreeBuilder treeBuilder = new DuTreeBuilder();

        DuDirectory actual = (DuDirectory) treeBuilder.buildTree(rootPath, new HashMap<>());

        DuTreeElement root = dir("/foo");
        DuTreeElement link = link("/foo/link", root);
        root.children().add(link);

        DuDirectory expected = (DuDirectory) tree(fs, root);

        Assert.assertEquals(expected.path().toString(), actual.path().toString());
        Assert.assertEquals(
                expected.children().get(0).path().toString(),
                actual.children().get(0).path().toString());
        Assert.assertEquals(
                expected.children().get(0).path().toString(),
                actual.children().get(0).path().toString());
        Assert.assertEquals(
                ((DuSymlink) expected.children().get(0)).getTarget().path().toString(),
                ((DuSymlink) actual.children().get(0)).getTarget().path().toString());
    }

    /*
     * structure:
     *   /foo
     *     /bar
     *       link1 -> ../baz
     *     /baz
     *       link2 -> ../bar
     */
    @Test
    public void testSymlinksTargetToEachOthersParentDirectories() throws IOException {
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

        DuDirectory actual = (DuDirectory) treeBuilder.buildTree(rootPath, new HashMap<>());

        DuTreeElement dir1 = dir("/foo/bar", link("/foo/bar/link1"));
        DuTreeElement dir2 = dir("/foo/baz", link("/foo/baz/link2", dir1));
        dir1.children().get(0).children().add(dir2);

        DuDirectory expected = (DuDirectory) tree(fs, dir("/foo", dir1, dir2));

        Assert.assertEquals(expected.path(), actual.path());
        Assert.assertEquals(expected.children().get(0).path(), actual.children().get(0).path());
        Assert.assertEquals(
                ((DuDirectory) expected.children().get(0)).children().get(0).path(),
                ((DuDirectory) actual.children().get(0)).children().get(0).path());
        Assert.assertEquals(expected.children().get(1).path(), actual.children().get(1).path());
        Assert.assertEquals(
                ((DuDirectory) expected.children().get(1)).children().get(0).path(),
                ((DuDirectory) actual.children().get(1)).children().get(0).path());
    }
}
