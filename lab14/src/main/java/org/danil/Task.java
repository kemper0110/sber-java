package org.danil;

import java.util.concurrent.Callable;

public class Task<T> {
    @FunctionalInterface
    interface State<T> {
        T get() throws TaskException;
    }

    // Реализация паттерна `State`
    // Возможно, не лучшее решение для продакшн, но для учебных целей совместил паттерны `DCL` и `State`
    volatile private State<T> state;
    private final Callable<? extends T> callable;

    // Начальное состояние - всегда захватывать монитор и вызывать callable.
    protected class InitialState implements State<T> {
        @Override
        public T get() throws TaskException {
            // В состоянии InitialState всегда происходит проверка на instanceof
            // после замены состояния, проверки прекращаются навсегда
            if (state instanceof InitialState) {
                synchronized (this) {
                    if (state instanceof InitialState) {
                        try {
                            final var result = callable.call();
                            // замена на состояние, всегда возвращающее результат
                            state = () -> result;
                        } catch (Exception ex) {
                            final var exception = new TaskException(ex);
                            // замена на состояние всегда бросающее исключение
                            state = () -> { throw exception; };
                        }
                    }
                }
            }
            // здесь у каждого потока будет новый объект состояния
            return Task.this.state.get();
        }
    }

    public Task(Callable<? extends T> callable) {
        this.callable = callable;
        this.state = new InitialState();
    }

    public T get() throws TaskException {
        // просто делегация вызова
        return state.get();
    }
}

