/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.tree;

import io.vavr.collection.Vector;

public final class JavaFile {
    public final JavaPackageDeclaration packageDeclaration;
    public final Vector<JavaImportDeclaration> importDeclarations;
    public final Vector<JavaClassDeclaration> classDeclarations;

    public JavaFile(
            JavaPackageDeclaration packageDeclaration,
            Vector<JavaImportDeclaration> importDeclarations,
            Vector<JavaClassDeclaration> classDeclarations) {
        this.packageDeclaration = packageDeclaration;
        this.importDeclarations = importDeclarations;
        this.classDeclarations = classDeclarations;
    }
}
