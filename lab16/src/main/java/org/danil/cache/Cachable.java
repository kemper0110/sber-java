package org.danil.cache;


import org.danil.serializer.JsonSerializer;
import org.danil.source.Source;
import org.danil.serializer.Serializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cachable {
    Class<? extends Source> value();
    Class<? extends Serializer> serializer() default JsonSerializer.class;
}
