package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.expr.Name;
import io.vavr.collection.Vector;

public record JavaName(Vector<JavaSimpleName> names) {
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
    public String toString() {
        return names.mkString(".");
    }
}
