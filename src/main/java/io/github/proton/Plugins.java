package io.github.proton;

import io.github.proton.display.Controller;
import io.github.proton.display.Projection;
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

    public static Projection projection() {
        return getExtensions(Projection.class).foldRight(Projection.unit, Projection::combine);
    }

    public static Controller controller() {
        return getExtensions(Controller.class).foldRight(Controller.unit, Controller::combine);
    }
}
