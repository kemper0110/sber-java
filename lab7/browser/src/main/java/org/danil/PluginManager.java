package org.danil;

import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.net.URLClassLoader;

@RequiredArgsConstructor
public class PluginManager {
    private final String pluginRootDirectory;

    public Plugin load(String pluginName, String pluginClassName) {
        try {
            final var classloader = new URLClassLoader(new URL[]{
               new URL(pluginRootDirectory + "/" + pluginName)
            });
            return (Plugin) classloader.loadClass(pluginClassName).getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
