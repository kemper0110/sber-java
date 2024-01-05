package org.example.models;

import org.example.exceptions.pin.PinDigitsException;
import org.example.exceptions.pin.PinLengthException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PinValidatorTest {
    @Test
    void validateExcessSize() {
        final var validator = new PinValidator();
        final var pins = new String[]{
                "1234233",
                "12344",
                "141244",
        };
        for (String pin : pins)
            assertThrowsExactly(PinLengthException.class, () -> validator.validate(pin));
    }
    @Test
    void validateNotDigits() {
        final var validator = new PinValidator();
        final var pins = new String[]{
                "abob",
                "12a",
                "123a",
                "12a2",
                "a223",
        };
        for (String pin : pins)
            assertThrowsExactly(PinDigitsException.class, () -> validator.validate(pin));
    }

    @Test
    void validateLess4Digits() {
        final var validator = new PinValidator();

        final var pins = new String[]{
                "1234",
                "123",
                "1",
                "12",
                "223",
        };

        for (String pin : pins)
            assertDoesNotThrow(() -> validator.validate(pin));
    }
}