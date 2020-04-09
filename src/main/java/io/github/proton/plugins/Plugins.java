package io.github.proton.plugins;

import io.vavr.collection.Vector;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

public final class Plugins {
    private static final PluginManager pluginManager = new DefaultPluginManager();

    public static void start() {
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
    }

    public static <T> Vector<T> getExtensions(Class<T> clazz) {
        return Vector.ofAll(pluginManager.getExtensions(clazz));
    }
}
