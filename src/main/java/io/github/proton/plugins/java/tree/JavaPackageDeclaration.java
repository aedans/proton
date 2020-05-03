/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.tree;

import java.util.Objects;

public final class JavaPackageDeclaration {
    public final JavaIdentifier name;

    public JavaPackageDeclaration(JavaIdentifier name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaPackageDeclaration that = (JavaPackageDeclaration) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
