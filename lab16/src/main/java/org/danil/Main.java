package org.danil;

import org.danil.cache.CacheConfigFactory;
import org.danil.cache.CacheProxy;
import org.danil.source.PostgresDBSource;
import org.danil.source.Source;
import org.postgresql.ds.PGSimpleDataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Main {

    public static void main(String... ar) {

        try {
            final var url = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=postgres";
            final var dataSource = new PGSimpleDataSource() {{
                setUrl(url);
            }};


            final var postgresConnection = dataSource.getConnection();
            final var cacheProxy = new CacheProxy();
            final var calculator = (Calculator) cacheProxy.cache(new CalculatorImpl(), new CacheConfigFactory() {
                final Map<Class<? extends Source>, Function<String, ? extends Source>> sourceMap = new HashMap<>(){{
                    put(PostgresDBSource.class, (name) -> new PostgresDBSource(postgresConnection, name));
                }};

                @Override
                public Source createSource(Class<? extends Source> sourceClass, String name) throws ReflectiveOperationException {
                    final var factory = sourceMap.get(sourceClass);
                    if (factory != null)
                        return factory.apply(name);
                    return CacheConfigFactory.super.createSource(sourceClass, name);
                }
            });

            calculator.fibonacci(10);
            calculator.fibonacci(11);
            calculator.fibonacci(11);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
