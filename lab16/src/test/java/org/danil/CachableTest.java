package org.danil;

import org.danil.cache.CacheConfigFactory;
import org.danil.cache.CacheProxy;
import org.danil.serializer.JsonSerializer;
import org.danil.serializer.SerializerException;
import org.danil.source.Source;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CachableTest {
    @Test
    void cachableTest() throws ReflectiveOperationException, SerializerException {

        final var cacheProxy = new CacheProxy();

        final var mockedSource = mock(Source.class);
        final var mockedFactory = mock(CacheConfigFactory.class);
        when(mockedFactory.createSerializer(any())).thenReturn(new JsonSerializer());
        final var serializer = mockedFactory.createSerializer(JsonSerializer.class);

        when(mockedFactory.createSource(any(), any())).thenReturn(mockedSource);
        final var calculator = (Calculator) cacheProxy.cache(new CalculatorImpl(), mockedFactory);

        // первый вызов с вычислением
        when(mockedSource.get(any())).thenReturn(null);
        final var res = calculator.fibonacci(10);
        verify(mockedFactory, times(1)).createSource(any(), any());
        verify(mockedSource, times(1)).get(any());
        verify(mockedSource, times(1)).put(any(), any());


        // второй вызов, уже используя кеша
        when(mockedSource.get(any())).thenReturn(serializer.stringify(res));
        calculator.fibonacci(10);
        verify(mockedFactory, times(1)).createSource(any(), any());
        verify(mockedSource, times(2)).get(any());
        verify(mockedSource, times(1)).put(any(), any());
    }
}
