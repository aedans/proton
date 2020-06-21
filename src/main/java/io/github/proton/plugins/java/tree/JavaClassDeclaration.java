package io.github.proton.plugins.java.tree;

import io.vavr.collection.Vector;

public record JavaClassDeclaration(JavaIdentifier name, Vector<JavaFieldMember>fields) {
}
