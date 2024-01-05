package org.example.models;

import org.example.exceptions.NotEnoughMoneyException;
import org.example.exceptions.lock.LockException;

public interface TerminalServer {
    long get() throws LockException;
    long put(long amount) throws LockException;
    long pull(long amount) throws NotEnoughMoneyException, LockException;
    boolean unlock(String code) throws LockException;
}
