package io.github.proton.display;

import io.github.proton.util.Registry;

public interface Renderer<T> {
    Registry<Renderer> registry = new Registry<>("renderer");
    @SuppressWarnings("unchecked")
    Renderer<Object> renderer = (o) -> registry.get(o.getClass()).render(o);

    Screen render(T t);
}
