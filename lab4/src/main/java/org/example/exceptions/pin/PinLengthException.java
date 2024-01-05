package org.example.exceptions.pin;

public class PinLengthException extends PinValidationException{
    public PinLengthException() {
        super("Пин-код должен содержать 4 цифры");
    }
    public PinLengthException(String message) {
        super(message);
    }
    public PinLengthException(Throwable cause) {
        super(cause);
    }
    public PinLengthException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
