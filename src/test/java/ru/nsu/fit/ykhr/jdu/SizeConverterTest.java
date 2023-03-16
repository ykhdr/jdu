package ru.nsu.fit.ykhr.jdu;

import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.ykhdr.jdu.utils.SizeConverter;
import ru.nsu.fit.ykhr.jdu.core.DuTest;

public class SizeConverterTest extends DuTest {

    // CR: max long test

    @Test
    public void zeroByteTest() {
        Assert.assertEquals("0.0 B", SizeConverter.convertToString(0));
    }

    @Test
    public void oneAndHalfKiBTest() {
        long BYTES_IN_KIB = 1024;
        Assert.assertEquals("1.5 KiB", SizeConverter.convertToString(BYTES_IN_KIB + (long) (0.5 * BYTES_IN_KIB)));
    }

    @Test
    public void TenAndQuarterMiBTest() {
        long BYTES_IN_MIB = 1048576;
        Assert.assertEquals("10.2 MiB", SizeConverter.convertToString(BYTES_IN_MIB * 10 + (long) (0.25 * BYTES_IN_MIB)));
    }

    @Test
    public void oneHundredFiveAndOneTenthGiBTest() {
        long BYTES_IN_GIB = 1073741824;
        Assert.assertEquals("104.1 GiB", SizeConverter.convertToString(104 * BYTES_IN_GIB + (long) (0.1 * BYTES_IN_GIB)));
    }
}
