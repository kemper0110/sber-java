package org.example.exceptions.pin;

public class PinMaxLengthException extends PinValidationException{
    public PinMaxLengthException() {
        super("Пин-код должен содержать 4 цифры");
    }
    public PinMaxLengthException(String message) {
        super(message);
    }
    public PinMaxLengthException(Throwable cause) {
        super(cause);
    }
    public PinMaxLengthException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
