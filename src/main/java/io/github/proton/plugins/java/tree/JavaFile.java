package io.github.proton.plugins.java.tree;

import io.vavr.collection.Vector;

public record JavaFile(JavaPackageDeclaration packageDeclaration,
                       Vector<JavaImportDeclaration>importDeclarations,
                       Vector<JavaClassDeclaration>classDeclarations) {
}
