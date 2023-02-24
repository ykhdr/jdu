package ru.nsu.fit.ykhdr.model;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.DuUtils;

import java.io.File;
import java.util.List;

public class Directory implements FileObj {
    @Override
    public List<FileObj> children() {
        return childrenList;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int depth() {
        return 0;
    }


    // TODO: 22.02.2023 нужно как то делать обход. В конструкторе сразу?
    public Directory(@NotNull File file) {
        this.file = file;
        childrenTraversal();
        //TODO: здесь нужно заполнять childrenList
    }

    private void childrenTraversal() {
        for (File childFile : file.listFiles()) {
            FileObj childrenFileObj = DuUtils.createFileObj(childFile);

            size += childrenFileObj.size();
            childrenList.add(childrenFileObj);
        }
    }

    private List<FileObj> childrenList;
    private final File file;
    private int size;
}
