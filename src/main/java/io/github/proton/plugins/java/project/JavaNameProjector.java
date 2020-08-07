package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import org.pf4j.Extension;

@Extension
public final class JavaNameProjector implements Projector<JavaName> {
    @Override
    public Class<JavaName> clazz() {
        return JavaName.class;
    }

    @Override
    public Projection<JavaName> project(JavaName name) {
        return new VectorProjection<>(
            name.names(),
            Projector.get(JavaSimpleName.class)::project,
            new JavaSimpleName(""),
            TextProjection.dot,
            JavaSimpleName::isEmpty,
            x -> x == '.'
        ).map(JavaName::new);
    }
}
