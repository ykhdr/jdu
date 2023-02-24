package ru.nsu.fit.ykhdr;

import java.io.IOException;

public class DuIOException extends RuntimeException{
    public DuIOException(IOException e){
        super(e);
    }
}
