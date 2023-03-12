package ru.nsu.fit.ykhdr.jdu.exception;

/**
 * Thrown if the given option argument on the command line should be numeric, but is not.
 */
public class DuNumberFormatException extends DuException{
    public DuNumberFormatException(String message, String parameter) {
        super(message + " : " + parameter);
    }

    public DuNumberFormatException(String message, int number) {
        super(message + " : " + number);
    }
}