package ru.nsu.fit.ykhdr;

import ru.nsu.fit.ykhdr.model.*;

import java.io.File;

public class DuUtils {
    public static FileObj createFileObj(File file) {
        if (!file.exists()) {
            throw new RuntimeException("something wrong...");
        }

        //TODO: провeрить, входит ли сюда symlink
        if (file.isFile()) {
            return new RegularFile(file);
        }
        else if (file.isDirectory()) {
            return new Directory(file);
        }
        else {
            return new Symlink(file);
        }
    }
}
