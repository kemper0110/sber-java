package org.danil.lab17;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@Builder
@Getter
@Setter
public class SimpleCompletionHandler<T> {
    Consumer<T> completed;
    @Builder.Default
    Consumer<Throwable> failed = th -> System.out.println(th.toString());
}
