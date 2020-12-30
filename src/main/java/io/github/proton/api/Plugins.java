package io.github.proton.api;

import io.vavr.Tuple2;
import io.vavr.collection.*;
import io.vavr.control.Option;
import org.pf4j.*;

public final class Plugins {
    public interface Combinable<T> extends Comparable<Combinable<T>> {
        T combine(T t);

        default int priority() {
            return 0;
        }

        @Override
        default int compareTo(Combinable<T> o) {
            return Integer.compare(priority(), o.priority());
        }
    }

    public interface ForClass {
        Class<?> clazz();
    }

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

    public static <T extends ForClass> Map<Class<?>, T> getExtensionMap(Class<T> clazz) {
        return getExtensions(clazz).toMap(t -> new Tuple2<>(t.clazz(), t));
    }

    public static <T extends ForClass, A> T getExtensionFor(Class<T> clazz, Class<A> c) {
        return getExtensionForOption(clazz, c).getOrNull();
    }

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