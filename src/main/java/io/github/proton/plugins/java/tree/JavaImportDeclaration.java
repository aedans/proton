/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.tree;

import java.util.Objects;

public final class JavaImportDeclaration {
    public final JavaIdentifier name;

    public JavaImportDeclaration(JavaIdentifier name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaImportDeclaration that = (JavaImportDeclaration) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
