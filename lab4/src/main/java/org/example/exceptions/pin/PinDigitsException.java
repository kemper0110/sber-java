package org.example.exceptions.pin;

public class PinDigitsException extends PinValidationException{
    public PinDigitsException() {
        super("Пин-код должен содержать только цифры");
    }
    public PinDigitsException(String message) {
        super(message);
    }
    public PinDigitsException(Throwable cause) {
        super(cause);
    }
    public PinDigitsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
