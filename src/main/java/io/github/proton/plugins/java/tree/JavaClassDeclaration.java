/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.tree;

import io.vavr.collection.Vector;
import java.util.Objects;

public final class JavaClassDeclaration {
    public final JavaIdentifier name;
    public final Vector<JavaFieldMember> fields;

    public JavaClassDeclaration(JavaIdentifier name, Vector<JavaFieldMember> fields) {
        this.name = name;
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaClassDeclaration that = (JavaClassDeclaration) o;
        return Objects.equals(name, that.name) && Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fields);
    }
}
