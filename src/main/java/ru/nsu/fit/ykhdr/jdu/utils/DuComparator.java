package ru.nsu.fit.ykhdr.jdu.utils;

import ru.nsu.fit.ykhdr.jdu.model.DuFile;

import java.util.Comparator;

/**
 * Comparator that comparing DuFile's by size
 */
public class DuComparator implements Comparator<DuFile> {

    @Override
    public int compare(DuFile o1, DuFile o2) {
        return -Long.compare(o1.size(), o2.size());
    }
}
