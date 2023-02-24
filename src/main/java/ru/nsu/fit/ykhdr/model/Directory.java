package ru.nsu.fit.ykhdr.model;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.DuUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Directory implements FileObj {
    @Override
    public List<FileObj> children() {
        return childrenList;
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


    public Directory(@NotNull File file) {
        this.file = file;
        childrenTraversal();
    }

    //TODO: переделать с учетом глубины (?)
    private void childrenTraversal() {
        for (File childFile : file.listFiles()) {
            FileObj childrenFileObj = DuUtils.createFileObj(childFile);

            size += childrenFileObj.size();
            childrenList.add(childrenFileObj);
        }
    }

    private final List<FileObj> childrenList = new ArrayList<>();
    private final File file;
    private long size = 0;

    private int depth = 0;
}
