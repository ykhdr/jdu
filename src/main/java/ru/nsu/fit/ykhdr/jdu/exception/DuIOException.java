package ru.nsu.fit.ykhdr.jdu.exception;

import java.io.IOException;

/**
 * Thrown in case of throwing IOException.
 */
public class DuIOException extends DuException {
    public DuIOException(IOException e) {
        super(e);
    }
}