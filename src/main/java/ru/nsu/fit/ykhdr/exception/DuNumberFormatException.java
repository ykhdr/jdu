package ru.nsu.fit.ykhdr.exception;

public class DuNumberFormatException extends DuException{
    public DuNumberFormatException(String message, String parameter) {
        super(message + " : " + parameter);
    }

    public DuNumberFormatException(String message, int number) {
        super(message + " : " + number);
    }
}