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
        if (t == null && !clazz.equals(Object.class)) {
            if (clazz.getSuperclass() != null) {
                t = get(clazz.getSuperclass());
                if (t != null)
                    return t;
            }
            Class[] interfaces = clazz.getInterfaces();
            for (Class c : interfaces) {
                t = get(c);
                if (t != null)
                    return t;
            }
        }
        return t;
    }

    public T getOrThrow(Class clazz) {
        T t = get(clazz);
        if (t == null)
            throw new RuntimeException("Could not find " + clazz + " in " + name + " registry");
        return t;
    }

    public void put(Class clazz, T t) {
        map.put(clazz, t);
    }

    @Override
    public String toString() {
        return name + map;
    }
}
