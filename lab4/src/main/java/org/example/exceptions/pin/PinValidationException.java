package org.example.exceptions.pin;

public class PinValidationException extends ValidationException {
    public PinValidationException() {
        super();
    }
    public PinValidationException(String message) {
        super(message);
    }
    public PinValidationException(Throwable cause) {
        super(cause);
    }
    public PinValidationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
