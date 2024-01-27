package org.danil;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class EncryptedClassLoader extends ClassLoader {
    private final byte key;
    private final File dir;

    public EncryptedClassLoader(byte key, File dir, ClassLoader parent) {
        super(parent);
        this.key = key;
        this.dir = dir;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        final var classfile = new File(dir, name);

        try (var inputStream = new FileInputStream(classfile)) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int length; (length = inputStream.read(buffer)) != -1; ) {
                for(int i = 0; i < length; ++i)
                    buffer[i] -= key;
                result.write(buffer, 0, length);
            }
            final var decryptedBytes = result.toByteArray();

            return defineClass(null, decryptedBytes, 0, decryptedBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("FileReader error", e);
        }
    }
}
