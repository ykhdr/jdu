package ru.nsu.fit.ykhdr.exception;

import org.jetbrains.annotations.NotNull;

public class DuArgumentException extends DuException {

    public DuArgumentException(@NotNull String message, @NotNull String argName) {
        super("du " + argName + ": " + message);
    }

}
