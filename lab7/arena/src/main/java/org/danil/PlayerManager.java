package org.danil;

import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.net.URLClassLoader;

@RequiredArgsConstructor
public class PlayerManager {
    private final String pluginRootDirectory;

    public RockPaperScissorsPlayer load(String pluginPath, String pluginClassName) {
        try {
            final var classloader = new URLClassLoader(new URL[]{new URL(pluginRootDirectory + "/" + pluginPath)});
            return (RockPaperScissorsPlayer) classloader.loadClass(pluginClassName).getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}