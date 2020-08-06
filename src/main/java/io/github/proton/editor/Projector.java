package io.github.proton.editor;

import io.github.proton.plugins.*;
import org.pf4j.ExtensionPoint;

public interface Projector<T> extends ExtensionPoint, ForClass<T> {
    @SuppressWarnings("unchecked")
    static <T> Projector<T> get(Class<T> clazz) {
        return Plugins.getExtensionFor(Projector.class, clazz);
    }

    Projection<T> project(T t);
}
