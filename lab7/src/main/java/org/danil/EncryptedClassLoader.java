package org.danil;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class EncryptedClassLoader extends ClassLoader {
    private final String key;
    private final File dir;

    public EncryptedClassLoader(String key, File dir, ClassLoader parent) {
        super(parent);
        this.key = key;
        this.dir = dir;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        final var classfile = new File(dir, name + ".class");

        try (var inputStream = new FileInputStream(classfile)) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for (int length; (length = inputStream.read(buffer)) != -1; )
                result.write(buffer, 0, length);
            final var encryptedClassCode = result.toString(StandardCharsets.UTF_8);

            final var decryptedClassCode = encryptedClassCode.replace(key, "java");

            final var decryptedBytes = decryptedClassCode.getBytes(StandardCharsets.UTF_8);

            return defineClass(name, decryptedBytes, 0, decryptedBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("FileReader error", e);
        }
    }
}
