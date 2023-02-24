package ru.nsu.fit.ykhdr.model;

import java.io.File;
import java.util.List;

public class RegularFile implements FileObj {
    @Override
    public List<FileObj> children() {
        return null;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public int depth() {
        return 0;
    }

    @Override
    public String name() {
        return file.getName();
    }


    public RegularFile(File file) {
        this.file = file;
        this.size = file.length();
    }

    private final File file;
    private long size;
}
