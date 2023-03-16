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
            GIGABYTE("GiB");

            private final String name;

            Dimension(String name) {
                this.name = name;
            }
        }

        private int integerPart = 0;
        private int fractionalPart = 0;
        private Dimension dimension;

        private static final int DELIMITER = 1024;

        FractionNumber(long bytes){
            convertBytes(bytes);
        }

        private void convertBytes(long bytes){
            int degree = 0;
            int remainder = 0;

            while (bytes / DELIMITER > 0) {
                remainder = (int) (bytes % DELIMITER);
                bytes /= DELIMITER;
                degree++;
            }

            fractionalPart = remainder / 100;
            integerPart = (int) bytes;
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