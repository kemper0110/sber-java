package org.danil.lab20.terminal.component;


import org.danil.lab20.terminal.exceptions.lock.AccountIsLockedException;
import org.danil.lab20.terminal.exceptions.lock.LockException;
import org.danil.lab20.terminal.exceptions.lock.TerminalIsLockedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class TerminalServerLockImpl implements TerminalServerLock {
    private final String pin;
    private final int accountLockTimeSeconds;
    private LockState state = new TerminalLockedState();

    public TerminalServerLockImpl(@Value("${terminal.pin}") String pin, @Value("${terminal.account-lock-time}") int accountLockTimeSeconds) {
        this.pin = pin;
        this.accountLockTimeSeconds = accountLockTimeSeconds;
    }

    @Override
    public boolean unlock(String pin) throws LockException {
        return state.unlock(pin);
    }

    @Override
    public void access() throws LockException {
        state.access();
    }

    private void setState(LockState state) {
        this.state = state;
    }


    // Различные состояния блокировки сервера
    interface LockState {
        boolean unlock(String pin) throws LockException;

        void access() throws LockException;
    }

    class UnlockedState implements LockState {
        @Override
        public boolean unlock(String pin) throws LockException {
            return true;
        }

        @Override
        public void access() throws LockException {
        }
    }

    class TerminalLockedState implements LockState {
        int attempts = 0;

        @Override
        public boolean unlock(String pin) throws LockException {
            if (!pin.equals(TerminalServerLockImpl.this.pin)) {
                if (++attempts >= 3) {
                    setState(new AccountLockedState());
                    throw new AccountIsLockedException(accountLockTimeSeconds);
                }
                return false;
            } else {
                setState(new UnlockedState());
                return true;
            }
        }

        @Override
        public void access() throws LockException {
            throw new TerminalIsLockedException();
        }
    }

    class AccountLockedState implements LockState {
        private final Date lockedAt = new Date();

        @Override
        public boolean unlock(String pin) throws LockException {
            access();
            return TerminalServerLockImpl.this.unlock(pin);
        }

        @Override
        public void access() throws LockException {
            final var elapsedSeconds = (new Date().getTime() - lockedAt.getTime()) / 1000;
            if (elapsedSeconds >= accountLockTimeSeconds) {
                setState(new TerminalLockedState());
            } else {
                throw new AccountIsLockedException((int) elapsedSeconds);
            }
        }
    }
}