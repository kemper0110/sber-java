package org.example.models;

import org.example.exceptions.lock.LockException;
import org.example.exceptions.NotEnoughMoneyException;

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
    public long put(long amount) throws LockException {
        lock.access();
        return account += amount;
    }

    @Override
    public long pull(long amount) throws NotEnoughMoneyException, LockException {
        lock.access();
        if (account < amount)
            throw new NotEnoughMoneyException();
        return account -= amount;
    }

    @Override
    public boolean unlock(String code) throws LockException {
        return lock.unlock(code);
    }
}
