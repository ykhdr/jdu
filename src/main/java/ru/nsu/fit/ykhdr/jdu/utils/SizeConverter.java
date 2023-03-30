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

//        private static FractionNumber fromBytes(long bytes) {
//            convertBytes();
//            return new FractionNumber(.., .. );
//        }

        private void convertBytes() {
            int degree = 0;

            while (integerPart / DELIMITER > 0) {
                fractionalPart = (int) (integerPart % DELIMITER);
                integerPart /= DELIMITER;
                // CR: break when degree is too big
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
     * Converts bytes to readable format, e.g. 2048 -> 2.0 MiB.
     * <p/>
     * Max first two fraction digits are shown, rounding down (won't show the second one if its zero).
     * <br/>
     * Suffix ranges from B (bytes) to TB (terabytes).
     */
    public static @NotNull String convertToString(long bytes) {
        return new FractionNumber(bytes).toString();
    }
}