package org.danil.lab20.terminal.service;


import org.danil.lab20.terminal.exceptions.account.TerminalOperationException;
import org.danil.lab20.terminal.exceptions.lock.LockException;

public interface TerminalServer {
    long get() throws LockException, TerminalOperationException;
    long put(long amount) throws LockException, TerminalOperationException;
    long pull(long amount) throws LockException, TerminalOperationException;
    boolean unlock(String code) throws LockException;
}
