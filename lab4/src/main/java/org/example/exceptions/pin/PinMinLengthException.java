package org.example.exceptions.pin;

public class PinMinLengthException extends PinLengthException {
    public PinMinLengthException() {
        super("Пин-код должен содержать 4 цифры");
    }
    public PinMinLengthException(String message) {
        super(message);
    }
    public PinMinLengthException(Throwable cause) {
        super(cause);
    }
    public PinMinLengthException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
