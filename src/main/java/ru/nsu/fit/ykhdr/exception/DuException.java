package ru.nsu.fit.ykhdr.exception;

/**
 * Instances should describe exceptions related to building options and using implementations of the DuFile interface.
 */

public abstract class DuException extends RuntimeException{
    public DuException(String message) {
        super(message);
    }

    public DuException(Throwable cause) {
        super(cause);
    }
}