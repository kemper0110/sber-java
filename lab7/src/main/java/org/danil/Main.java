package org.danil;

import java.io.File;

class Main {
    public static void main(String... args) {
        final var encryptedClassLoader = new EncryptedClassLoader("lava", new File("C:\\Users\\Danil\\IdeaProjects\\sber-java\\lab7\\target\\classes"), Main.class.getClassLoader());

        try {
            final var EncryptedClass = encryptedClassLoader.loadClass("EncryptedClass");
            final var ctor = EncryptedClass.getConstructor();
            final Runnable object = (Runnable) ctor.newInstance();
            object.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}