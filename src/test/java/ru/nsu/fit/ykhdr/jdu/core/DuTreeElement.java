package ru.nsu.fit.ykhdr.jdu.core;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.jdu.model.*;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.*;

public record DuTreeElement(Type type, String path, List<DuTreeElement> children) {

    public static DuFile tree(FileSystem fs, DuTreeElement root) {
        return buildTree(root, fs.getPath("/"), new HashSet<>());
    }

    private static DuFile buildTree(DuTreeElement treeElement, Path parentPath, Set<Path> visited) {
        Path currentPath = parentPath.resolve(treeElement.path);

        if (treeElement.type == Type.FILE) {
            return new DuRegularFile(currentPath, 0);
        }

        if (treeElement.type == Type.DIR) {
            List<DuFile> duChildren = treeElement.children.stream().map(c -> buildTree(c, currentPath, visited)).toList();
            return new DuDirectory(currentPath, duChildren, size(duChildren));
        }

        Path targetPath = Path.of(treeElement.children().get(0).path);
        DuSymlink symlink;
        if (!visited.add(currentPath)) {
            symlink = new DuSymlink(currentPath, new DuUnknownFile(targetPath, 0), 0);
        } else {
            DuFile target = treeElement.children.stream().map(c -> buildTree(c, targetPath, visited)).toList().get(0);
            symlink = new DuSymlink(currentPath, target, target.size());
        }

        return symlink;
    }

    public static DuTreeElement dir(String name, DuTreeElement... files) {
        return new DuTreeElement(Type.DIR, name, new ArrayList<>(List.of(files)));
    }

    public static DuTreeElement file(String name) {
        return new DuTreeElement(Type.FILE, name, null);
    }

    public static DuTreeElement link(String name, DuTreeElement... files) {
        return new DuTreeElement(Type.LINK, name, new ArrayList<>(List.of(files)));
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

