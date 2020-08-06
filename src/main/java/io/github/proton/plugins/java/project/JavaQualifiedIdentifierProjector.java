package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import org.pf4j.Extension;

@Extension
public final class JavaQualifiedIdentifierProjector implements Projector<JavaQualifiedIdentifier> {
    @Override
    public Class<JavaQualifiedIdentifier> clazz() {
        return JavaQualifiedIdentifier.class;
    }

    @Override
    public Projection<JavaQualifiedIdentifier> project(JavaQualifiedIdentifier qualifiedIdentifier) {
        return new VectorProjection<>(
            qualifiedIdentifier.names(),
            Projector.get(JavaName.class)::project,
            new JavaName(""),
            TextProjection.dot,
            JavaName::isEmpty,
            x -> x == '.'
        ).map(JavaQualifiedIdentifier::new);
    }
}
