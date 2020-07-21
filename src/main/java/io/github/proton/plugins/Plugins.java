package io.github.proton.plugins;

import io.github.proton.editor.Theme;
import io.vavr.collection.Vector;
import org.pf4j.*;

public final class Plugins {
    private static final PluginManager pluginManager = new DefaultPluginManager();

    public static void start() {
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
    }

    public static <T> Vector<T> getExtensions(Class<T> clazz) {
        return Vector.ofAll(pluginManager.getExtensions(clazz));
    }

    private static Theme theme = Plugins.getExtensions(Theme.class).get(0);

    public static Theme getTheme() {
        return theme;
    }
}
