package ru.nsu.fit.ykhdr.jdu.utils;

import ru.nsu.fit.ykhdr.jdu.model.DuFile;

import java.util.Comparator;

/**
 * Comparator that comparing DuFile's by size
 */
public class DuComparator implements Comparator<DuFile> {

    /**
     * @param o1 the first DuFile to be compared.
     * @param o2 the second DuFile to be compared.
     * @return the value 0 if o1.size() == o2.size();
     *         a value greater than 0 if o1.size() < o2.size();
     *         a value less than 0 if o1.size() > o2.size().
     */
    @Override
    public int compare(DuFile o1, DuFile o2) {
        return -Long.compare(o1.size(), o2.size());
    }
}
