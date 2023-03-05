package ru.nsu.fit.ykhdr.utils;

import org.jetbrains.annotations.NotNull;

/**
 *  Utility class designed to convert the number of bytes to higher in the hierarchy of units in the string representation.
 */

public class SizeConverter {
    private static final int DELIMITER = 1024;

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

    /**
     * Converts the number of bytes to a string representation of the size in a more appropriate dimension.
     * <p>
     * @param size
     *        number of bytes to convert.
     * <p>
     * @return
     *        String representation of the converted size.
     */

    public static @NotNull String convertToString(long size) {
        return convertToDesiredSize(size) + " " + matchDimension(size);
    }

    private static @NotNull String matchDimension(long size) {
        return switch (countDegree(size)) {
            case 0 -> Dimension.BYTE.name;
            case 1 -> Dimension.KILOBYTE.name;
            case 2 -> Dimension.MEGABYTE.name;
            default -> Dimension.GIGABYTE.name;
        };
    }

    private static @NotNull String convertToDesiredSize(long size) {
        int remainder = 0;
        while (size / DELIMITER > 0) {
            remainder = (int) (size % DELIMITER);
            size /= DELIMITER;
        }

        return size + "." + remainder / 100;
    }

    private static int countDegree(long number) {
        int degree = 0;

        while (number / DELIMITER > 0) {
            number /= DELIMITER;
            degree++;
        }

        return degree;
    }
}