/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.tree;

import java.util.Objects;

public final class JavaFieldMember {
    public final JavaType type;
    public final JavaIdentifier name;

    public JavaFieldMember(JavaType type, JavaIdentifier name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaFieldMember that = (JavaFieldMember) o;
        return Objects.equals(type, that.type) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name);
    }
}
