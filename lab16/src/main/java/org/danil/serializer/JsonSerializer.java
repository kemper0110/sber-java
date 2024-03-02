package org.danil.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerializer implements Serializer{
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T parse(String s, Class<T> tClass) throws SerializerException {
        try {
            return objectMapper.readValue(s, tClass);
        } catch (Exception e) {
            throw new SerializerException(e);
        }
    }

    @Override
    public String stringify(Object o) throws SerializerException {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new SerializerException(e);
        }
    }
}
