package ru.nsu.fit.ykhdr.exception;

public abstract class DuException extends RuntimeException{
    public DuException(String message) {
        super(message);
    }

    public DuException(Throwable cause) {
        super(cause);
    }
}