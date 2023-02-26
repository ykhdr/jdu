package ru.nsu.fit.ykhdr.utils;

import org.jetbrains.annotations.NotNull;

public class SizeConverter {
    private final static int DELIMITER = 1024;

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

    public static @NotNull String convertToString(long size) {
        return convertToDesiredSize(size) + " " + matchDimension(size);
    }

    private static @NotNull String matchDimension(long size) {
        switch (countDegree(size)) {
            case 0 -> {
                return Dimension.BYTE.name;
            }
            case 1 -> {
                return Dimension.KILOBYTE.name;
            }
            case 2 -> {
                return Dimension.MEGABYTE.name;
            }
            default -> {
                return Dimension.GIGABYTE.name;
            }
        }
    }

    // TODO: 26.02.2023 продумать как можно избежать повторения кода (сделать специальную структуру для числа?)
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
