package io.github.proton.display;

import io.github.proton.util.Registry;

public interface Renderer<T> {
    Registry<Renderer> registry = new Registry<>("renderer");
    @SuppressWarnings("unchecked")
    Renderer<Object> renderer = (o, selected) -> registry.getOrThrow(o.getClass()).render(o, selected);

    Screen render(T t, boolean selected);
}
