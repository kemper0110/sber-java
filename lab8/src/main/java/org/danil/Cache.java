package org.danil;

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

    boolean[] identityBy() default {};
}
