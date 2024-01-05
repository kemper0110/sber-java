package org.example.models;

import org.example.exceptions.pin.PinDigitsException;
import org.example.exceptions.pin.PinLengthException;
import org.example.exceptions.pin.PinValidationException;

public class PinValidator {
    void validate(String pin) throws PinValidationException {
        if(!pin.matches("^\\d+$"))
            throw new PinDigitsException();
        if(pin.length() > 4)
            throw new PinLengthException();
    }
}
