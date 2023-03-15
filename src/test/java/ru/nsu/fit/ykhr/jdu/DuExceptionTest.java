package ru.nsu.fit.ykhr.jdu;

import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.ykhdr.jdu.exception.DuArgumentException;
import ru.nsu.fit.ykhdr.jdu.exception.DuIOException;
import ru.nsu.fit.ykhdr.jdu.exception.DuNumberFormatException;
import ru.nsu.fit.ykhr.jdu.core.DuTest;

import java.io.IOException;

// CR: remove
public class DuExceptionTest extends DuTest {
    @Test
    public void duArgumentExceptionTest() {
        DuArgumentException argumentException = new DuArgumentException("DuArgumentException message");
        Assert.assertEquals("DuArgumentException message", argumentException.getMessage());
    }

    @Test
    public void duIOExceptionTest() {
        DuIOException duIOException1 = new DuIOException("DuIOException message");

        IOException ioException = new IOException();
        DuIOException duIOException2 = new DuIOException(ioException);

        Assert.assertEquals("DuIOException message", duIOException1.getMessage());
        Assert.assertEquals(ioException, duIOException2.getCause());
    }

    @Test
    public void duNumberFormatException() {
        DuNumberFormatException duNumberFormatException1 = new DuNumberFormatException("message", "parameter");
        DuNumberFormatException duNumberFormatException2 = new DuNumberFormatException("message", 100);

        Assert.assertEquals("message : parameter",duNumberFormatException1.getMessage());
        Assert.assertEquals("message : 100",duNumberFormatException2.getMessage());
    }
}
