/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.tree;

import io.vavr.collection.Vector;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaFile javaFile = (JavaFile) o;
        return Objects.equals(packageDeclaration, javaFile.packageDeclaration)
                && Objects.equals(importDeclarations, javaFile.importDeclarations)
                && Objects.equals(classDeclarations, javaFile.classDeclarations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageDeclaration, importDeclarations, classDeclarations);
    }
}
