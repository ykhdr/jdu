package ru.nsu.fit.ykhdr.jdu.exception;

/**
 * Base class for du related exceptions.
 */
public abstract class DuException extends RuntimeException {
    public DuException(String message) {
        super(message);
    }

    public DuException(Throwable cause) {
        super(cause);
    }
}