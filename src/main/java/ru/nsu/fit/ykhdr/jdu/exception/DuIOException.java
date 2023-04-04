package ru.nsu.fit.ykhdr.jdu.exception;

import java.io.IOException;

/**
 * Thrown in case of IO problems.
 */
public class DuIOException extends DuException {
    public DuIOException(IOException e) {
        super(e);
    }
}