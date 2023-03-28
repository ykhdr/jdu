package ru.nsu.fit.ykhdr.jdu.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Utility class designed to convert the number of bytes to higher in the hierarchy of units in the string representation.
 */

public class SizeConverter {
    private static class FractionNumber {
        private enum Dimension {
            BYTE("B"),
            KILOBYTE("KiB"),
            MEGABYTE("MiB"),
            GIGABYTE("GiB"),
            TERABYTE("TB");

            private final String name;

            Dimension(String name) {
                this.name = name;
            }
        }

        private long integerPart;
        private int fractionalPart = 0;
        private Dimension dimension;

        private static final int DELIMITER = 1024;

        FractionNumber(long bytes) {
            integerPart = bytes;
            convertBytes();
        }

        private void convertBytes() {
            int degree = 0;

            while (integerPart / DELIMITER > 0) {
                fractionalPart = (int) (integerPart % DELIMITER);
                integerPart /= DELIMITER;
                degree++;
            }

            fractionalPart /= 100;
            dimension = Dimension.values()[degree];
        }


        @Override
        public String toString() {
            return integerPart + "."+ fractionalPart + " " + dimension.name;
        }
    }

    /**
     * Converts the number of bytes to a string representation of the size in a more appropriate dimension.
     * <p>
     *
     * @param size number of bytes to convert.
     *             <p>
     * @return String representation of the converted size.
     */

    public static @NotNull String convertToString(long size) {
        return new FractionNumber(size).toString();
    }
}