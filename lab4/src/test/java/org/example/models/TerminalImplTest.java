package org.example.models;

import org.example.exceptions.account.NotEnoughMoneyException;
import org.example.exceptions.lock.AccountIsLockedException;
import org.example.exceptions.lock.TerminalIsLockedException;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class TerminalImplTest {
    @Test
    void test() {
        final var pin = "1234";
        final int lockTimeSeconds = 1, sleepTimeMilliseconds = lockTimeSeconds * 1100;

        final Terminal terminal = new TerminalImpl(new TerminalServerImpl(new TerminalServerLockImpl(pin, lockTimeSeconds)));

        assertThrowsExactly(TerminalIsLockedException.class, terminal::get);
        assertThrowsExactly(TerminalIsLockedException.class, () -> terminal.put(100));
        assertThrowsExactly(TerminalIsLockedException.class, () -> terminal.pull(100));


        assertDoesNotThrow(() -> assertFalse(terminal.unlock("1233")));
        assertDoesNotThrow(() -> assertFalse(terminal.unlock("1233")));
        assertThrowsExactly(AccountIsLockedException.class, () -> terminal.unlock("1233"));
        assertThrowsExactly(AccountIsLockedException.class, () -> terminal.unlock("1233"));
        assertThrowsExactly(AccountIsLockedException.class, () -> terminal.unlock("1233"));

        assertDoesNotThrow(() -> Thread.sleep(sleepTimeMilliseconds));
        assertDoesNotThrow(() -> assertTrue(terminal.unlock(pin)));

        assertDoesNotThrow(() -> {
            assertEquals(0, terminal.get());

            assertEquals(200, terminal.put(200));
            assertEquals(100, terminal.pull(100));

            assertThrowsExactly(NotEnoughMoneyException.class, () -> terminal.pull(200));
        });
    }
}