package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.expr.Name;
import io.github.proton.editor.*;
import io.vavr.collection.Vector;

public record JavaName(Vector<JavaSimpleName> names) implements Tree<JavaName> {
    public static JavaName from(Name name) {
        return new JavaName(name
            .getQualifier()
            .map(JavaName::from)
            .orElseGet(() -> new JavaName(Vector.empty()))
            .names
            .append(new JavaSimpleName(name.getIdentifier())));
    }

    public boolean isEmpty() {
        return names.isEmpty() || names.get().isEmpty();
    }

    @Override
    public Projection<JavaName> project() {
        return new VectorProjection<>(
            names,
            new JavaSimpleName(""),
            TextProjection.dot,
            JavaSimpleName::isEmpty,
            x -> x == '.'
        ).map(JavaName::new);
    }

    @Override
    public String toString() {
        return names.mkString(".");
    }
}
