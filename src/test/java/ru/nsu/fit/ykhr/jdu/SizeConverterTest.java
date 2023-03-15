package ru.nsu.fit.ykhr.jdu;

import org.junit.Assert;
import org.junit.Test;
import ru.nsu.fit.ykhdr.jdu.utils.SizeConverter;
import ru.nsu.fit.ykhr.jdu.core.DuTest;

public class SizeConverterTest extends DuTest {
    private final long BYTES_IN_KIB = 1024;
    private final long BYTES_IN_MIB = 1048576;
    private final long BYTES_IN_GIB = 1073741824;

    // CR: max long test

    @Test
    public void zeroByteTest() {
        Assert.assertEquals("0.0 B", SizeConverter.convertToString(0));
    }

    @Test
    public void oneKiBTest() {
        Assert.assertEquals("1.0 KiB", SizeConverter.convertToString(BYTES_IN_KIB));
    }

    @Test
    public void oneAndHalfKiBTest() {
        Assert.assertEquals("1.5 KiB", SizeConverter.convertToString(BYTES_IN_KIB + (long) (0.5 * BYTES_IN_KIB)));
    }

    @Test
    public void oneMiBTest() {
        Assert.assertEquals("1.0 MiB", SizeConverter.convertToString(BYTES_IN_MIB));
    }

    @Test
    public void TenAndQuarterMiBTest() {
        Assert.assertEquals("10.2 MiB", SizeConverter.convertToString(BYTES_IN_MIB * 10 + (long) (0.25 * BYTES_IN_MIB)));
    }

    @Test
    public void sixtyFourAndNineTenthsGiBTest() {
        Assert.assertEquals("64.9 GiB", SizeConverter.convertToString(64 * BYTES_IN_GIB + (long) (0.9 * BYTES_IN_GIB)));
    }

    @Test
    public void oneHundredFiveAndOneTenthGiBTest() {
        Assert.assertEquals("104.1 GiB", SizeConverter.convertToString(104 * BYTES_IN_GIB + (long) (0.1 * BYTES_IN_GIB)));
    }
}
