package org.danil;

import java.io.*;

class EncryptedMain {
    public static void main(String... args) throws FileNotFoundException {
        // replace with your path
        final var dir = "C:\\Users\\Danil\\IdeaProjects\\sber-java\\lab7\\target\\classes\\org\\danil";
        final byte key = 1;


        // Encrypt normal class
        try (
                final var in = new FileInputStream(new File(dir, "NormalClass.class"));
                final var out = new FileOutputStream(new File(dir, "EncryptedClass.class"))
        ) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            final var buffer = new byte[4096];
            for (int length; (length = in.read(buffer)) != -1; ) {
                for(int i = 0; i < length; ++i)
                    buffer[i] += key;
                result.write(buffer, 0, length);
            }
            final var outbuf = result.toByteArray();
            out.write(outbuf, 0, outbuf.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // decrypt recently encrypted class

        final var encryptedClassLoader = new EncryptedClassLoader(key, new File(dir), EncryptedMain.class.getClassLoader());

        try {
            final var EncryptedClass = encryptedClassLoader.findClass("EncryptedClass.class");
            final var ctor = EncryptedClass.getConstructor();
            final Runnable object = (Runnable) ctor.newInstance();
            object.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}