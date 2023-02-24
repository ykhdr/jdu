package ru.nsu.fit.ykhdr;

import ru.nsu.fit.ykhdr.model.Directory;
import ru.nsu.fit.ykhdr.model.DuConfig;
import ru.nsu.fit.ykhdr.model.FileObj;
import ru.nsu.fit.ykhdr.model.RegularFile;

import java.util.List;

public class DuPrinter {
    public DuPrinter(DuConfig config) {
        this.config = config;
    }

    public void printDirectoryTree() {
        int depth = 0;
        System.out.println("\t".repeat(depth) + "/" + config.root().name() + " [" + config.root().size() + "]");
        printChildren(config.root().children(), depth + 1);
    }

    private void printDirectoryTree(Directory directory, int depth) {
        System.out.println("\t".repeat(depth) + "/" + directory.name() + " [" + directory.size() + "]");
        printChildren(directory.children(), depth + 1);
    }

    private void printRegularFile(RegularFile file, int depth) {
        System.out.println("\t".repeat(depth) + file.name() + " [" + file.size() + "]");
    }

    private void printChildren(List<FileObj> children, int depth) {
        if (depth > config.depth()) {
            return;
        }

        for (FileObj child : children) {
            if (child instanceof Directory) {
                printDirectoryTree((Directory) child, depth);
            }
            else if (child instanceof RegularFile) {
                printRegularFile((RegularFile) child, depth);
            }
        }
    }

    //TODO: добавить принт симлинков

    private DuConfig config;
}
