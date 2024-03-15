package org.danil.lab20.terminal.component;


import org.danil.lab20.terminal.exceptions.lock.LockException;

public interface TerminalServerLock {
    boolean unlock(String pin) throws LockException;
    void access() throws LockException;
}
