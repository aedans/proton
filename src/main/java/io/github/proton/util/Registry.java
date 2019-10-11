package io.github.proton.util;

import java.util.HashMap;
import java.util.Map;

public final class Registry<T> {
    private final Map<Class, T> map = new HashMap<>();
    private final String name;

    public Registry(String name) {
        this.name = name;
    }

    public T get(Class clazz) {
        T t = map.get(clazz);
        if (t == null)
            throw new RuntimeException("Could not find " + clazz + " in " + name + " registry");
        return t;
    }

    public void put(Class clazz, T t) {
        map.put(clazz, t);
    }

    @Override
    public String toString() {
        return name + super.toString();
    }
}
