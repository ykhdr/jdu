package ru.nsu.fit.ykhdr.jdu.exception;

import java.io.IOException;

/**
 * Thrown in case of throwing IOException or AccessDeniedException.
 */
public class DuIOException extends DuException {
    public DuIOException(IOException e) {
        super(e);
    }

    public DuIOException(String message) {
        super(message);
    }
}