package ru.nsu.fit.ykhdr.exception;

/**
 * Throws if command line arguments were entered incorrectly.
 */
public class DuArgumentException extends DuException {
    public DuArgumentException(String message) {
        super(message);
    }
}