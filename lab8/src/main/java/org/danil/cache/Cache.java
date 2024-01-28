package org.danil.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cache {
    enum CacheType {
        FILE, IN_MEMORY
    }
    CacheType cacheType() default CacheType.IN_MEMORY;

    String filenamePrefix() default "";

    boolean zip() default false;

    /**
     * Битовый массив определяющий учитывать параметр или нет.
     * Если массив не передан, то учитываются все аргументы.
     * Если указан пустой массив, то не учитывает аргументы вовсе.
     * Аргументы, для которых не указано true/false не учитываются.
     */
    boolean[] identityBy() default {};

    /**
     * Максимальная длинна кешируемого списка.
     * Если метод возвращает список длинной больше указанной, то он не будет кеширован.
     */
    int maxListSize() default Integer.MAX_VALUE;
}
