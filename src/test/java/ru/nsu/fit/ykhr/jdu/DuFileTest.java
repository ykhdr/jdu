package ru.nsu.fit.ykhr.jdu;

import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.ykhdr.jdu.model.DuDirectory;
import ru.nsu.fit.ykhdr.jdu.model.DuRegularFile;
import ru.nsu.fit.ykhdr.jdu.model.DuSymlink;
import ru.nsu.fit.ykhr.jdu.core.DuTest;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DuFileTest extends DuTest {
    @Test
    public void regularFileTest() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("foo.txt");
        Files.createFile(fooPath);

        DuRegularFile regularFile = new DuRegularFile(fooPath, Files.size(fooPath));

        Assert.assertEquals(0, regularFile.size());
        Assert.assertEquals(fooPath, regularFile.path());
        Assert.assertEquals("foo.txt",regularFile.name());
    }

    @Test
    public void directoryTest() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("/foo");
        Files.createDirectory(fooPath);
        DuDirectory directory1 = new DuDirectory(fooPath, new ArrayList<>(), Files.size(fooPath));

        Path barPath = fs.getPath("/bar");
        Files.createDirectory(barPath);
        Path bazPath = barPath.resolve("baz");
        Files.createDirectory(bazPath);
        DuDirectory directoryBaz = new DuDirectory(bazPath,new ArrayList<>(),Files.size(bazPath));
        DuDirectory directory2 = new DuDirectory(barPath, List.of(directoryBaz),Files.size(bazPath));

        Assert.assertEquals(fooPath, directory1.path());
        Assert.assertEquals("[]", directory1.children().toString());
        Assert.assertEquals(0, directory1.size());
        Assert.assertEquals("foo",directory1.name());

        Assert.assertEquals(barPath, directory2.path());
        Assert.assertEquals("[DuDirectory[path=/bar/baz, children=[], size=0]]", directory2.children().toString());
        Assert.assertEquals(0, directory2.size());
        Assert.assertEquals("bar",directory2.name());
    }

    @Test
    public void symlinkTest() throws IOException {
        FileSystem fs = fileSystem();
        Path dirPath = fs.getPath("/dir");
        Files.createDirectory(dirPath);
        Path link1Path = fs.getPath("link1");
        Files.createSymbolicLink(link1Path,dirPath);
        DuDirectory directory = new DuDirectory(dirPath, new ArrayList<>(), Files.size(dirPath));
        DuSymlink link1 = new DuSymlink(link1Path, List.of(directory), Files.size(link1Path));

        Path filePath = fs.getPath("file.txt");
        Files.createFile(filePath);
        Path link2Path = fs.getPath("link2");
        Files.createSymbolicLink(link2Path,filePath);
        DuSymlink link2 = new DuSymlink(link2Path,new ArrayList<>(),Files.size(link2Path));

        Assert.assertEquals(link1Path, link1.path());
        Assert.assertEquals("[DuDirectory[path=/dir, children=[], size=0]]", link1.children().toString());
        Assert.assertEquals(0, link1.size());
        Assert.assertEquals("link1",link1.name());

        Assert.assertEquals(link2Path, link2.path());
        Assert.assertEquals("[]", link2.children().toString());
        Assert.assertEquals(0, link2.size());
        Assert.assertEquals("link2",link2.name());
    }
}
