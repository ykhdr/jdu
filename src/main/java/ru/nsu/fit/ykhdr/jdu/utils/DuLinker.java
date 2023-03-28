package ru.nsu.fit.ykhdr.jdu.utils;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.ykhdr.jdu.model.DuFile;
import ru.nsu.fit.ykhdr.jdu.model.DuSymlink;
import ru.nsu.fit.ykhdr.jdu.model.DuUnknownFile;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public class DuLinker {
    public static void linkSymlinks(@NotNull Map<Path, DuFile> treeNodes) {
        for (Map.Entry<Path, DuFile> entry : treeNodes.entrySet()) {
            if (entry.getValue() instanceof DuSymlink symlink) {
                DuFile target = treeNodes.get(symlink.getTargetPath());
                symlink.setTarget(Objects.requireNonNullElseGet(target, () -> new DuUnknownFile(symlink.getTargetPath())));
            }
        }
    }
}
