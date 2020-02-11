package io.github.proton;

import io.github.proton.display.Component;
import io.github.proton.display.Controller;
import io.github.proton.display.Projection;
import io.vavr.collection.Vector;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

import java.util.Comparator;

public final class Plugins {
    private static final PluginManager pluginManager = new DefaultPluginManager();

    public static void start() {
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
    }

    public static <T> Vector<T> getExtensions(Class<T> clazz) {
        return Vector.ofAll(pluginManager.getExtensions(clazz));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Component> T projectDefault(Object tree) {
        return (T) project(tree).get();
    }

    public static <T extends Component> Vector<T> project(Object tree) {
        return projection().projectGeneric(tree);
    }

    private static Projection projection = null;
    public static Projection projection() {
        if (projection == null) {
            projection = getExtensions(Projection.class)
                    .sorted(Comparator.comparingInt(Projection::precedence).reversed())
                    .foldRight(Projection.unit, Projection::combine);
        }
        return projection;
    }

    private static Controller controller = null;
    public static Controller controller() {
        if (controller == null) {
            controller = getExtensions(Controller.class)
                    .sorted(Comparator.comparingInt(Controller::precedence).reversed())
                    .foldRight(Controller.unit, Controller::combine);
        }
        return controller;
    }
}
