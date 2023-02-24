package ru.nsu.fit.ykhdr.model;

import java.util.List;

public interface FileObj {
    public List<FileObj> children();

    public long size();

    public int depth();

    public String name();
}
