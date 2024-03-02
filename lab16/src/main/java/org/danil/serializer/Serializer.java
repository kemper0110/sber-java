package org.danil.serializer;

public interface Serializer {
    <T> T parse(String s, Class<T> tClass) throws SerializerException;
    String stringify(Object o) throws SerializerException;
}
