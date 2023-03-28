package ru.nsu.fit.ykhdr.jdu;

import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.ykhdr.jdu.core.DuTest;
import ru.nsu.fit.ykhdr.jdu.exception.DuArgumentException;
import ru.nsu.fit.ykhdr.jdu.exception.DuNumberFormatException;
import ru.nsu.fit.ykhdr.jdu.options.DuOptions;
import ru.nsu.fit.ykhdr.jdu.options.OptionsBuilder;

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

        Assert.assertEquals(12, OptionsBuilder.build(args).limit());
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

    @Test
    public void emptyParameterDepthOptionTest() {
        String[] args = new String[]{"--depth"};

        DuArgumentException thrown = Assert.assertThrows(
                DuArgumentException.class,
                () -> OptionsBuilder.build(args));

        String expectedMessage = "Missing argument for option: depth";
        String actualMessage = thrown.getMessage();

        Assert.assertEquals(expectedMessage,actualMessage);
    }

    @Test
    public void nonNumericParameterDepthOptionTest() {
        String[] args = new String[]{"--depth", "a"};

        DuNumberFormatException thrown = Assert.assertThrows(
                DuNumberFormatException.class,
                () -> OptionsBuilder.build(args));

        String expectedMessage = "this parameter isn't a number : a";
        String actualMessage = thrown.getMessage();

        Assert.assertEquals(expectedMessage, actualMessage);

    }

    @Test
    public void negativeNumberParameterDepthOptionTest() {
        String[] args = new String[]{"--depth", "-100"};

        DuNumberFormatException thrown = Assert.assertThrows(
                DuNumberFormatException.class,
                () -> OptionsBuilder.build(args));

        String expectedMessage = "incorrectly number entered : -100";
        String actualMessage = thrown.getMessage();

        Assert.assertEquals(expectedMessage,actualMessage);
    }

    @Test
    public void emptyParameterLimitOptionTest() {
        String[] args = new String[]{"--limit"};

        DuArgumentException thrown = Assert.assertThrows(
                DuArgumentException.class,
                () -> OptionsBuilder.build(args));

        String expectedMessage = "Missing argument for option: limit";
        String actualMessage = thrown.getMessage();

        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void nonNumericParameterLimitOptionTest() {
        String[] args3 = new String[]{"--limit", "a"};

        DuNumberFormatException thrown = Assert.assertThrows(
                DuNumberFormatException.class,
                () -> OptionsBuilder.build(args3));

        String expectedMessage = "this parameter isn't a number : a";
        String actualMessage = thrown.getMessage();

        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void negativeNumberParameterLimitOptionTest() {
        String[] args = new String[]{"--limit", "-100"};

        DuNumberFormatException thrown = Assert.assertThrows(
                DuNumberFormatException.class,
                () -> OptionsBuilder.build(args));

        String expectedMessage = "incorrectly number entered : -100";
        String actualMessage = thrown.getMessage();

        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void incorrectlyPathEntered() throws IOException {
        FileSystem fs = fileSystem();
        Path foo = fs.getPath("foo");
        Path bar = fs.getPath("bar");

        Files.createDirectory(foo);
        Files.createDirectory(bar);

        String[] args = new String[]{"foo", "bar"};

        DuArgumentException thrown = Assert.assertThrows(
                DuArgumentException.class,
                () -> OptionsBuilder.build(args));


        String expectedMessage = "Multiple root path entered";
        String actualMessage = thrown.getMessage();

        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void incorrectlyOptionTest() {
        String[] args1 = new String[]{"--depthh", "0"};
        String[] args2 = new String[]{"-A"};
        String[] args3 = new String[]{"--option", "a"};

        DuArgumentException thrown1 = Assert.assertThrows(
                DuArgumentException.class,
                () -> OptionsBuilder.build(args1));
        DuArgumentException thrown2 = Assert.assertThrows(
                DuArgumentException.class,
                () -> OptionsBuilder.build(args2));
        DuArgumentException thrown3 = Assert.assertThrows(
                DuArgumentException.class,
                () -> OptionsBuilder.build(args3));

        String expectedMessage1 = "Unrecognized option: --depthh";
        String actualMessage1 = thrown1.getMessage();

        String expectedMessage2 = "Unrecognized option: -A";
        String actualMessage2 = thrown2.getMessage();

        String expectedMessage3 = "Unrecognized option: --option";
        String actualMessage3 = thrown3.getMessage();

        Assert.assertEquals(actualMessage1, expectedMessage1);
        Assert.assertEquals(actualMessage2, expectedMessage2);
        Assert.assertEquals(actualMessage3, expectedMessage3);
    }
}