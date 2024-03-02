package org.danil.cache;

import org.danil.source.Source;
import org.danil.serializer.Serializer;

public interface CacheConfigFactory {
    default Source createSource(Class<? extends Source> sourceClass, String name) throws ReflectiveOperationException {
        return sourceClass.getConstructor(String.class).newInstance(name);
    }
    default Serializer createSerializer(Class<? extends Serializer> serializerClass) throws ReflectiveOperationException {
        return serializerClass.getConstructor().newInstance();
    }
}
