# Домашнее задание №16
> Разработать продвинутый кэш, который помнит о кэшированных данных после перезапуска приложения.

```java
@interface Cachable {
    Class<? extends Source> value();
}

class Calculator {
    @Cachable(H2DB.class)
    public List<Integer> fibonachi(int i) {
        // алгоритм
    }
}
```