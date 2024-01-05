package org.example.models;

import org.example.exceptions.lock.LockException;
import org.example.exceptions.NotEnoughMoneyException;
import org.example.exceptions.pin.PinValidationException;

public interface Terminal {
    long get() throws LockException;
    long put(long amount) throws LockException;
    long pull(long amount) throws NotEnoughMoneyException, LockException;
    boolean unlock(String code) throws LockException, PinValidationException;
}
