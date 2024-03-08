package org.danil.lab17;

import lombok.RequiredArgsConstructor;

import java.nio.channels.CompletionHandler;

@RequiredArgsConstructor
public class SimpleCompletionAdapter<T> implements CompletionHandler<T, Object>{
    private final SimpleCompletionHandler<T> handler;

    @Override
    public void completed(T result, Object attachment) {
        handler.completed.accept(result);
    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        handler.failed.accept(exc);
    }
}
