package ru.nsu.fit.ykhdr.model;

import java.io.File;
import java.util.List;

public class Symlink implements FileObj {


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

    public Symlink(File file) {
        this.file = file;
    }

    private int size;
    private final File file;
}
