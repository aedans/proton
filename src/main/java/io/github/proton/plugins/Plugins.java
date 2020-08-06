package io.github.proton.plugins;

import io.vavr.Tuple2;
import io.vavr.collection.*;
import io.vavr.control.Option;
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

    public static <T extends Combinable<T>> T getExtension(Class<T> clazz) {
        return getExtensions(clazz).sorted().reduce(Combinable::combine);
    }

    @SuppressWarnings("rawtypes")
    public static <T extends ForClass> Map<Class, T> getExtensionMap(Class<T> clazz) {
        return getExtensions(clazz).toMap(t -> new Tuple2<>(t.clazz(), t));
    }

    @SuppressWarnings("rawtypes")
    public static <T extends ForClass, A> T getExtensionFor(Class<T> clazz, Class<A> c) {
        return getExtensionForOption(clazz, c).getOrNull();
    }

    @SuppressWarnings("rawtypes")
    public static <T extends ForClass, A> Option<T> getExtensionForOption(Class<T> clazz, Class<A> c) {
        return superclasses(c).map(Plugins.getExtensionMap(clazz)::get).reduce(Option::orElse);
    }

    private static <T> Vector<Class<?>> superclasses(Class<T> clazz) {
        if (clazz.getSuperclass() == null) {
            return Vector.of(clazz);
        } else {
            return Vector.<Class<?>>of(clazz)
                .appendAll(Vector.of(clazz.getInterfaces()).flatMap(Plugins::superclasses))
                .appendAll(superclasses(clazz.getSuperclass()));
        }
    }
}
