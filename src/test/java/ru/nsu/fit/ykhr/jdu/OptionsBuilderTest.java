package ru.nsu.fit.ykhr.jdu;

import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.ykhdr.jdu.options.DuOptions;
import ru.nsu.fit.ykhdr.jdu.options.OptionsBuilder;
import ru.nsu.fit.ykhr.jdu.core.DuTest;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

public class OptionsBuilderTest extends DuTest {
    @Test
    public void defaultOptionsTest() {
        String[] args = new String[]{};
        Path rootPath = Path.of("./");
        DuOptions options = OptionsBuilder.build(args);

        Assert.assertEquals(10, options.depth());
        Assert.assertEquals(5, options.limit());
        Assert.assertFalse(options.followSymlinks());
        Assert.assertEquals(rootPath, options.root());
    }

    @Test
    public void depthOptionTest() {
        String[] args = new String[]{"--depth", "11"};

        Assert.assertEquals(11, OptionsBuilder.build(args).depth());
    }

    @Test
    public void limitOptionTest() {
        String[] args = new String[]{"--limit", "12"};

        Assert.assertEquals(999999, OptionsBuilder.build(args).depth());
    }

    @Test
    public void followSymlinkOptionTest() {
        String[] args = new String[]{"-L"};

        Assert.assertTrue(OptionsBuilder.build(args).followSymlinks());
    }

    @Test
    public void pathOptionTest() throws IOException {
        FileSystem fs = fileSystem();
        Path fooPath = fs.getPath("/foo");
        Files.createDirectory(fooPath);

        String[] args1 = new String[]{};
        String[] args2 = new String[]{"/foo"};

        Assert.assertEquals(Path.of("./"), OptionsBuilder.build(args1).root());
        Assert.assertEquals(fooPath.toString(), OptionsBuilder.build(args2).root().toString());
    }

    // FIXME: 17.03.2023 переделать следующие тесты
//    @Test
//    public void incorrectlyDepthOptionTest() {
//        String[] args1 = new String[]{"--depth", "0"};
//        String[] args2 = new String[]{"--depth", "-100"};
//        String[] args3 = new String[]{"--depth", "a"};
//        String[] args4 = new String[]{"--depth"};
//
//        OptionsBuilder builder1 = new OptionsBuilder(args1);
//        OptionsBuilder builder2 = new OptionsBuilder(args2);
//        OptionsBuilder builder3 = new OptionsBuilder(args3);
//        OptionsBuilder builder4 = new OptionsBuilder(args4);
//
//        DuNumberFormatException thrown1 = Assert.assertThrows(
//                DuNumberFormatException.class,
//                builder1::build);
//        DuNumberFormatException thrown2 = Assert.assertThrows(
//                DuNumberFormatException.class,
//                builder2::build);
//        DuNumberFormatException thrown3 = Assert.assertThrows(
//                DuNumberFormatException.class,
//                builder3::build);
//        DuArgumentException thrown4 = Assert.assertThrows(
//                DuArgumentException.class,
//                builder4::build);
//
//        String expectedMessage1 = "incorrectly number entered : 0";
//        String actualMessage1 = thrown1.getMessage();
//
//        String expectedMessage2 = "incorrectly number entered : -100";
//        String actualMessage2 = thrown2.getMessage();
//
//        String expectedMessage3 = "this parameter isn't a number : a";
//        String actualMessage3 = thrown3.getMessage();
//
//        String expectedMessage4 = "Missing argument for option: depth";
//        String actualMessage4 = thrown4.getMessage();
//
//        Assert.assertEquals(actualMessage1, expectedMessage1);
//        Assert.assertEquals(actualMessage2, expectedMessage2);
//        Assert.assertEquals(actualMessage3, expectedMessage3);
//        Assert.assertEquals(actualMessage4, expectedMessage4);
//    }
//
//    @Test
//    public void incorrectlyLimitOptionTest() {
//        String[] args1 = new String[]{"--limit", "0"};
//        // CR: split into multiple tests
//        String[] args2 = new String[]{"--limit", "-100"};
//        String[] args3 = new String[]{"--limit", "a"};
//        String[] args4 = new String[]{"--limit"};
//
//        OptionsBuilder builder1 = new OptionsBuilder(args1);
//        OptionsBuilder builder2 = new OptionsBuilder(args2);
//        OptionsBuilder builder3 = new OptionsBuilder(args3);
//        OptionsBuilder builder4 = new OptionsBuilder(args4);
//
//        DuNumberFormatException thrown1 = Assert.assertThrows(
//                DuNumberFormatException.class,
//                builder1::build);
//        DuNumberFormatException thrown2 = Assert.assertThrows(
//                DuNumberFormatException.class,
//                builder2::build);
//        DuNumberFormatException thrown3 = Assert.assertThrows(
//                DuNumberFormatException.class,
//                builder3::build);
//        DuArgumentException thrown4 = Assert.assertThrows(
//                DuArgumentException.class,
//                builder4::build);
//
//        String expectedMessage1 = "incorrectly number entered : 0";
//        String actualMessage1 = thrown1.getMessage();
//
//        String expectedMessage2 = "incorrectly number entered : -100";
//        String actualMessage2 = thrown2.getMessage();
//
//        String expectedMessage3 = "this parameter isn't a number : a";
//        String actualMessage3 = thrown3.getMessage();
//
//        String expectedMessage4 = "Missing argument for option: limit";
//        String actualMessage4 = thrown4.getMessage();
//
//        Assert.assertEquals(actualMessage1, expectedMessage1);
//        Assert.assertEquals(actualMessage2, expectedMessage2);
//        Assert.assertEquals(actualMessage3, expectedMessage3);
//        Assert.assertEquals(actualMessage4, expectedMessage4);
//    }
//
//    @Test
//    public void incorrectlyPathEntered() throws IOException {
//        FileSystem fs = fileSystem();
//        Path foo = fs.getPath("foo");
//        Path bar = fs.getPath("bar");
//
//        Files.createDirectory(foo);
//        Files.createDirectory(bar);
//
//        String[] args = new String[]{"foo", "bar"};
//
//        OptionsBuilder builder = new OptionsBuilder(args);
//
//        DuArgumentException thrown = Assert.assertThrows(
//                DuArgumentException.class,
//                builder::build);
//
//
//        String expectedMessage = "Multiple root path entered";
//        String actualMessage = thrown.getMessage();
//
//        Assert.assertEquals(actualMessage, expectedMessage);
//    }
//
//    @Test
//    public void incorrectlyOptionTest() {
//        String[] args1 = new String[]{"--depthh", "0"};
//        String[] args2 = new String[]{"-A"};
//        String[] args3 = new String[]{"--option", "a"};
//
//        OptionsBuilder builder1 = new OptionsBuilder(args1);
//        OptionsBuilder builder2 = new OptionsBuilder(args2);
//        OptionsBuilder builder3 = new OptionsBuilder(args3);
//
//        DuArgumentException thrown1 = Assert.assertThrows(
//                DuArgumentException.class,
//                builder1::build);
//        DuArgumentException thrown2 = Assert.assertThrows(
//                DuArgumentException.class,
//                builder2::build);
//        DuArgumentException thrown3 = Assert.assertThrows(
//                DuArgumentException.class,
//                builder3::build);
//
//        String expectedMessage1 = "Unrecognized option: --depthh";
//        String actualMessage1 = thrown1.getMessage();
//
//        String expectedMessage2 = "Unrecognized option: -A";
//        String actualMessage2 = thrown2.getMessage();
//
//        String expectedMessage3 = "Unrecognized option: --option";
//        String actualMessage3 = thrown3.getMessage();
//
//        Assert.assertEquals(actualMessage1, expectedMessage1);
//        Assert.assertEquals(actualMessage2, expectedMessage2);
//        Assert.assertEquals(actualMessage3, expectedMessage3);
//    }
}