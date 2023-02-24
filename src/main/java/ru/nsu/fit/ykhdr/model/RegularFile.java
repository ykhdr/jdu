package ru.nsu.fit.ykhdr.model;

import java.io.File;
import java.util.List;

public class RegularFile implements FileObj {
    @Override
    public List<FileObj> children() {
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int depth() {
        return 0;
    }


    public RegularFile(File file) {
        this.file = file;
    }

    private final File file;
    private int size;
}
