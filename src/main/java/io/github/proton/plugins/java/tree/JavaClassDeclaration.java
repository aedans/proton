package io.github.proton.plugins.java.tree;

import io.vavr.collection.Vector;

public record JavaClassDeclaration(JavaIdentifier name,
                                   Vector<JavaMember> members) {
    public boolean isEmpty() {
        return name.isEmpty() && members.isEmpty();
    }
}
