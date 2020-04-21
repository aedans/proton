/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.tree;

import io.github.proton.plugins.text.Line;
import io.vavr.collection.Vector;

public final class JavaClassDeclaration {
    public final Line name;
    public final Vector<JavaFieldMember> fields;

    public JavaClassDeclaration(Line name, Vector<JavaFieldMember> fields) {
        this.name = name;
        this.fields = fields;
    }
}
