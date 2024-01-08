package org.example.models;

import org.example.exceptions.account.NotEnoughMoneyException;
import org.example.exceptions.account.TerminalOperationException;
import org.example.exceptions.lock.LockException;

public interface TerminalServer {
    long get() throws LockException, TerminalOperationException;
    long put(long amount) throws LockException, TerminalOperationException;
    long pull(long amount) throws LockException, TerminalOperationException;
    boolean unlock(String code) throws LockException;
}
