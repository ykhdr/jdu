package ru.nsu.fit.ykhdr;

import ru.nsu.fit.ykhdr.model.Directory;
import ru.nsu.fit.ykhdr.model.FileObj;
import ru.nsu.fit.ykhdr.model.RegularFile;
import ru.nsu.fit.ykhdr.model.Symlink;

import java.io.File;
import java.nio.file.Files;

public class DuUtils {
    public static FileObj createFileObj(File file) {
        if (!file.exists()) {
            throw new RuntimeException("something wrong...");
        }

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
