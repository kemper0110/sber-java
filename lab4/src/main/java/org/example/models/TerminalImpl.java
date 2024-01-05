package org.example.models;

import org.example.exceptions.lock.LockException;
import org.example.exceptions.NotEnoughMoneyException;
import org.example.exceptions.pin.PinValidationException;

public class TerminalImpl implements Terminal {
    private final PinValidator pinValidator;
    private final TerminalServer server;
    public TerminalImpl(TerminalServer server) {
        this.server = server;
        this.pinValidator = new PinValidator();
    }
    @Override
    public long get() throws LockException {
        return server.get();
    }

    @Override
    public long put(long amount) throws LockException {
        return server.put(amount);
    }

    @Override
    public long pull(long amount) throws NotEnoughMoneyException, LockException {
        return server.pull(amount);
    }

    @Override
    public boolean unlock(String code) throws LockException, PinValidationException {
        pinValidator.validate(code);
        return code.length() >= 4 && server.unlock(code);
    }
}
