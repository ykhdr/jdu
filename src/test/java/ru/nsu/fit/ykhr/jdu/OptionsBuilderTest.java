package ru.nsu.fit.ykhr.jdu;

import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.ykhdr.jdu.exception.DuArgumentException;
import ru.nsu.fit.ykhdr.jdu.exception.DuNumberFormatException;
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
        OptionsBuilder builder = new OptionsBuilder(args);
        DuOptions options = builder.build();

        Assert.assertEquals(10, options.depth());
        Assert.assertEquals(5, options.limit());
        Assert.assertFalse(options.followSymlinks());
        Assert.assertEquals(rootPath, options.root());
    }

    @Test
    public void depthOptionTest() {
        String[] args1 = new String[]{"--depth", "2"};
        String[] args2 = new String[]{"--depth", "105"};
        String[] args3 = new String[]{"--depth", "999999"};

        OptionsBuilder builder1 = new OptionsBuilder(args1);
        OptionsBuilder builder2 = new OptionsBuilder(args2);
        OptionsBuilder builder3 = new OptionsBuilder(args3);

        Assert.assertEquals(2, builder1.build().depth());
        Assert.assertEquals(105, builder2.build().depth());
        Assert.assertEquals(999999, builder3.build().depth());
    }

    @Test
    public void limitOptionTest() {
        String[] args1 = new String[]{"--limit", "2"};
        String[] args2 = new String[]{"--limit", "105"};
        String[] args3 = new String[]{"--limit", "999999"};

        OptionsBuilder builder1 = new OptionsBuilder(args1);
        OptionsBuilder builder2 = new OptionsBuilder(args2);
        OptionsBuilder builder3 = new OptionsBuilder(args3);

        Assert.assertEquals(2, builder1.build().limit());
        Assert.assertEquals(105, builder2.build().limit());
        Assert.assertEquals(999999, builder3.build().limit());
    }

    @Test
    public void followSymlinkOptionTest() {
        String[] args = new String[]{"-L"};

        OptionsBuilder builder = new OptionsBuilder(args);

        Assert.assertTrue(builder.build().followSymlinks());
    }

    @Test
    public void pathOptionTest() {
        String[] args = new String[]{};

        OptionsBuilder builder = new OptionsBuilder(args);

        Assert.assertEquals(Path.of("./"), builder.build().root());
    }

    @Test
    public void incorrectlyDepthOptionTest() {
        String[] args1 = new String[]{"--depth", "0"};
        String[] args2 = new String[]{"--depth", "-100"};
        String[] args3 = new String[]{"--depth", "a"};
        String[] args4 = new String[]{"--depth"};

        OptionsBuilder builder1 = new OptionsBuilder(args1);
        OptionsBuilder builder2 = new OptionsBuilder(args2);
        OptionsBuilder builder3 = new OptionsBuilder(args3);
        OptionsBuilder builder4 = new OptionsBuilder(args4);

        DuNumberFormatException thrown1 = Assert.assertThrows(
                DuNumberFormatException.class,
                builder1::build);
        DuNumberFormatException thrown2 = Assert.assertThrows(
                DuNumberFormatException.class,
                builder2::build);
        DuNumberFormatException thrown3 = Assert.assertThrows(
                DuNumberFormatException.class,
                builder3::build);
        DuArgumentException thrown4 = Assert.assertThrows(
                DuArgumentException.class,
                builder4::build);

        String expectedMessage1 = "incorrectly number entered : 0";
        String actualMessage1 = thrown1.getMessage();

        String expectedMessage2 = "incorrectly number entered : -100";
        String actualMessage2 = thrown2.getMessage();

        String expectedMessage3 = "this parameter isn't a number : a";
        String actualMessage3 = thrown3.getMessage();

        String expectedMessage4 = "Missing argument for option: depth";
        String actualMessage4 = thrown4.getMessage();

        Assert.assertEquals(actualMessage1, expectedMessage1);
        Assert.assertEquals(actualMessage2, expectedMessage2);
        Assert.assertEquals(actualMessage3, expectedMessage3);
        Assert.assertEquals(actualMessage4, expectedMessage4);
    }

    @Test
    public void incorrectlyLimitOptionTest() {
        String[] args1 = new String[]{"--limit", "0"};
        String[] args2 = new String[]{"--limit", "-100"};
        String[] args3 = new String[]{"--limit", "a"};
        String[] args4 = new String[]{"--limit"};

        OptionsBuilder builder1 = new OptionsBuilder(args1);
        OptionsBuilder builder2 = new OptionsBuilder(args2);
        OptionsBuilder builder3 = new OptionsBuilder(args3);
        OptionsBuilder builder4 = new OptionsBuilder(args4);

        DuNumberFormatException thrown1 = Assert.assertThrows(
                DuNumberFormatException.class,
                builder1::build);
        DuNumberFormatException thrown2 = Assert.assertThrows(
                DuNumberFormatException.class,
                builder2::build);
        DuNumberFormatException thrown3 = Assert.assertThrows(
                DuNumberFormatException.class,
                builder3::build);
        DuArgumentException thrown4 = Assert.assertThrows(
                DuArgumentException.class,
                builder4::build);

        String expectedMessage1 = "incorrectly number entered : 0";
        String actualMessage1 = thrown1.getMessage();

        String expectedMessage2 = "incorrectly number entered : -100";
        String actualMessage2 = thrown2.getMessage();

        String expectedMessage3 = "this parameter isn't a number : a";
        String actualMessage3 = thrown3.getMessage();

        String expectedMessage4 = "Missing argument for option: limit";
        String actualMessage4 = thrown4.getMessage();

        Assert.assertEquals(actualMessage1, expectedMessage1);
        Assert.assertEquals(actualMessage2, expectedMessage2);
        Assert.assertEquals(actualMessage3, expectedMessage3);
        Assert.assertEquals(actualMessage4, expectedMessage4);
    }

    @Test
    public void incorrectlyPathEntered() throws IOException {
        FileSystem fs = fileSystem();
        Path foo = fs.getPath("foo");
        Path bar = fs.getPath("bar");

        Files.createDirectory(foo);
        Files.createDirectory(bar);

        String[] args1 = new String[]{"baz"};
        String[] args2 = new String[]{"foo", "bar"};

        OptionsBuilder builder1 = new OptionsBuilder(args1);
        OptionsBuilder builder2 = new OptionsBuilder(args2);

        DuArgumentException thrown1 = Assert.assertThrows(
                DuArgumentException.class,
                builder1::build);
        DuArgumentException thrown2 = Assert.assertThrows(
                DuArgumentException.class,
                builder2::build);

        String expectedMessage1 = "File doesn't exist: baz";
        String actualMessage1 = thrown1.getMessage();

        String expectedMessage2 = "Multiple root path entered";
        String actualMessage2 = thrown2.getMessage();

        Assert.assertEquals(actualMessage1, expectedMessage1);
        Assert.assertEquals(actualMessage2, expectedMessage2);
    }

    @Test
    public void incorrectlyOptionTest() {
        String[] args1 = new String[]{"--depthh", "0"};
        String[] args2 = new String[]{"-A"};
        String[] args3 = new String[]{"--option", "a"};

        OptionsBuilder builder1 = new OptionsBuilder(args1);
        OptionsBuilder builder2 = new OptionsBuilder(args2);
        OptionsBuilder builder3 = new OptionsBuilder(args3);

        DuArgumentException thrown1 = Assert.assertThrows(
                DuArgumentException.class,
                builder1::build);
        DuArgumentException thrown2 = Assert.assertThrows(
                DuArgumentException.class,
                builder2::build);
        DuArgumentException thrown3 = Assert.assertThrows(
                DuArgumentException.class,
                builder3::build);

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