package org.danil.serializer;

import java.io.*;
import java.util.Base64;

public class JavaSerializer implements Serializer {
    @Override
    public <T> T parse(String s, Class<T> tClass) throws SerializerException {
        byte[] data = Base64.getDecoder().decode(s);
        try (var bais = new ByteArrayInputStream(data);
             var ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        } catch (Exception e) {
            throw new SerializerException(e);
        }
    }

    @Override
    public String stringify(Object o) throws SerializerException {
        try (var baos = new ByteArrayOutputStream();
             var oos = new ObjectOutputStream(baos)) {
            oos.writeObject(o);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new SerializerException(e);
        }
    }
}
