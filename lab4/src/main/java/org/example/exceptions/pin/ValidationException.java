package org.example.exceptions.pin;

public class ValidationException extends Exception{
    public ValidationException() {
        super();
    }
    public ValidationException(String message) {
        super(message);
    }
    public ValidationException(Throwable cause) {
        super(cause);
    }
    public ValidationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
