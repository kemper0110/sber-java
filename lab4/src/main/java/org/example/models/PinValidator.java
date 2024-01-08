package org.example.models;

import org.example.exceptions.pin.*;

public class PinValidator {
    void validate(String pin) throws PinValidationException {
        if(pin.length() < 4)
            throw new PinMinLengthException();
        if(pin.length() > 4)
            throw new PinMaxLengthException();
        if(!pin.matches("^\\d*$"))
            throw new PinDigitsException();
    }
}
