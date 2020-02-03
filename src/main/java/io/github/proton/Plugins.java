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

    private static Projection projection = null;
    public static Projection projection() {
        if (projection == null) {
            projection = getExtensions(Projection.class).foldRight(Projection.unit, Projection::combine);
        }
        return projection;
    }

    private static Controller controller = null;
    public static Controller controller() {
        if (controller == null) {
            controller = getExtensions(Controller.class).foldRight(Controller.unit, Controller::combine);
        }
        return controller;
    }
}
