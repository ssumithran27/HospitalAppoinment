package org.example.Exception;

public class MyClassException extends RuntimeException {
    public MyClassException(String message) {
        super(message);
    }
    public MyClassException(String message,Throwable cause){super(message, cause);}
}
