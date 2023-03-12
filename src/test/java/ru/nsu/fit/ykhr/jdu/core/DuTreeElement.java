package ru.nsu.fit.ykhr.jdu.core;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.jdu.model.DuDirectory;
import ru.nsu.fit.ykhdr.jdu.model.DuFile;
import ru.nsu.fit.ykhdr.jdu.model.DuRegularFile;
import ru.nsu.fit.ykhdr.jdu.model.DuSymlink;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record DuTreeElement(Type type, String path, List<DuTreeElement> children) {

    public static DuFile tree(FileSystem fs, DuTreeElement root) {
        return buildTree(root, fs.getPath("/"), new HashSet<>());
    }

    private static DuFile buildTree(DuTreeElement treeElement, Path parentPath, Set<Path> visited) {
        Path currentPath = parentPath.resolve(treeElement.path);

        if (treeElement.type == Type.FILE) {
            try {
                return new DuRegularFile(currentPath, Files.size(currentPath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (treeElement.type == Type.LINK) {
            if (!visited.add(currentPath)) {
                return new DuSymlink(currentPath, new ArrayList<>(), 0);
            }
        }

        if (treeElement.type == Type.DIR) {
            List<DuFile> duChildren = treeElement.children.stream().map(c -> buildTree(c, currentPath, visited)).toList();
            return new DuDirectory(currentPath, duChildren, size(duChildren));
        }

        List<DuFile> duChildren = treeElement.children.stream().map(c -> buildTree(c, currentPath, visited)).toList();
        return new DuSymlink(currentPath, duChildren, size(duChildren));
    }

    public static DuTreeElement dir(String name, DuTreeElement... files) {
        return new DuTreeElement(Type.DIR, name, List.of(files));
    }

    public static DuTreeElement file(String name) {
        return new DuTreeElement(Type.FILE, name, null);
    }

    public static DuTreeElement link(String name, DuTreeElement... files) {
        return new DuTreeElement(Type.LINK, name, List.of(files));
    }

    private static long size(@NotNull List<DuFile> children) {
        long size = 0;

        for (DuFile child : children) {
            if (!(child instanceof DuSymlink)) {
                size += child.size();
            }
        }
        return size;
    }

    private enum Type {
        DIR,
        FILE,
        LINK
    }
}

