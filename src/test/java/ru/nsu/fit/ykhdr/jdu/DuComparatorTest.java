package ru.nsu.fit.ykhdr.jdu;

import junit.framework.TestCase;
import org.junit.Test;
import ru.nsu.fit.ykhdr.jdu.model.DuFile;
import ru.nsu.fit.ykhdr.jdu.model.DuRegularFile;
import ru.nsu.fit.ykhdr.jdu.utils.DuComparator;
import ru.nsu.fit.ykhdr.jdu.core.DuTest;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class DuComparatorTest extends DuTest {
    @Test
    public void testDuComparator() throws IOException {
        FileSystem fs = fileSystem();

        Path foo1Path = fs.getPath("foo1");
        Files.createFile(foo1Path);
        Path bar1Path = fs.getPath("bar1");
        Files.createFile(bar1Path);
        DuFile foo1 = new DuRegularFile(foo1Path, 1000);
        DuFile bar1 = new DuRegularFile(bar1Path, 2000);

        Path foo2Path = fs.getPath("foo2");
        Files.createFile(foo2Path);
        Path bar2Path = fs.getPath("bar2");
        Files.createFile(bar2Path);
        DuFile foo2 = new DuRegularFile(foo2Path, 12221);
        DuFile bar2 = new DuRegularFile(bar2Path, 12221);

        DuComparator comparator = new DuComparator();

        TestCase.assertEquals(-Long.compare(foo1.size(), bar1.size()), comparator.compare(foo1, bar1));
        TestCase.assertEquals(-Long.compare(foo2.size(), bar2.size()), comparator.compare(foo2, bar2));
    }
}
