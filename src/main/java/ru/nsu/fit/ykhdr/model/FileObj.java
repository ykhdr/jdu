package ru.nsu.fit.ykhdr.model;

import java.util.List;

public interface FileObj {

    static int depth = 0;

    public List<FileObj> children();

    public int size();

    public int depth();
}
