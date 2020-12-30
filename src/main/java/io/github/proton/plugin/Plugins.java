package io.github.proton.plugin;

import org.pf4j.*;

import java.util.*;

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

    private static final PluginManager pluginManager = new DefaultPluginManager();

    public static void start() {
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
    }

    public static <T> List<T> getExtensions(Class<T> clazz) {
        return pluginManager.getExtensions(clazz);
    }

    public static <T extends Combinable<T>> T getExtension(Class<T> clazz, T orElse) {
        List<T> extensions = getExtensions(clazz);
        Collections.sort(extensions);
        return extensions.stream().reduce(Combinable::combine).orElse(orElse);
    }
}