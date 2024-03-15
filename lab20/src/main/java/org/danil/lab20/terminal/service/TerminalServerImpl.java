package org.danil.lab20.terminal.service;

import lombok.RequiredArgsConstructor;
import org.danil.lab20.terminal.exceptions.account.NegativeMoneyException;
import org.danil.lab20.terminal.exceptions.account.NotDivisibleException;
import org.danil.lab20.terminal.exceptions.account.NotEnoughMoneyException;
import org.danil.lab20.terminal.exceptions.account.TerminalOperationException;
import org.danil.lab20.terminal.exceptions.lock.LockException;
import org.danil.lab20.terminal.component.TerminalServerLock;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TerminalServerImpl implements TerminalServer {
    private long account = 0L;
    private final TerminalServerLock lock;

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
