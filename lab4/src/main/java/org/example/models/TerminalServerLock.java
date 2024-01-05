package org.example.models;

import org.example.exceptions.lock.LockException;

public interface TerminalServerLock {
    boolean unlock(String pin) throws LockException;
    void access() throws LockException;
}
