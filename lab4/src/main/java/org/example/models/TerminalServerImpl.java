package org.example.models;

import org.example.exceptions.account.*;
import org.example.exceptions.lock.LockException;

public class TerminalServerImpl implements TerminalServer {
    private long account = 0L;
    private final TerminalServerLock lock;

    public TerminalServerImpl(TerminalServerLock lock) {
        this.lock = lock;
    }

    @Override
    public long get() throws LockException {
        lock.access();
        return account;
    }

    @Override
    public long put(long amount) throws LockException, TerminalOperationException {
        lock.access();
        if(amount < 0)
            throw new NegativeMoneyException();
        if(amount % 100 != 0)
            throw new NotDivisibleException();
        return account += amount;
    }

    @Override
    public long pull(long amount) throws LockException, TerminalOperationException {
        lock.access();
        if(amount < 0)
            throw new NegativeMoneyException();
        if(amount % 100 != 0)
            throw new NotDivisibleException();
        if (account < amount)
            throw new NotEnoughMoneyException();
        return account -= amount;
    }

    @Override
    public boolean unlock(String code) throws LockException {
        return lock.unlock(code);
    }
}
