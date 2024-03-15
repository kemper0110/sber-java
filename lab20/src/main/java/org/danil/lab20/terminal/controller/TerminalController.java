package org.danil.lab20.terminal.controller;

import lombok.RequiredArgsConstructor;
import org.danil.lab20.terminal.exceptions.account.TerminalOperationException;
import org.danil.lab20.terminal.exceptions.lock.LockException;
import org.danil.lab20.terminal.service.TerminalServer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/terminal")
@RequiredArgsConstructor
public class TerminalController {
    private final TerminalServer terminalServer;

    @PostMapping("/unlock")
    boolean unlock(@RequestBody String pin) throws LockException {
        return terminalServer.unlock(pin);
    }

    @GetMapping
    long get() throws LockException, TerminalOperationException {
        return terminalServer.get();
    }

    @PostMapping("/pull")
    long pull(@RequestBody long amount) throws LockException, TerminalOperationException {
        return terminalServer.pull(amount);
    }

    @PostMapping("/put")
    long put(@RequestBody long amount) throws LockException, TerminalOperationException {
        return terminalServer.put(amount);
    }
}
