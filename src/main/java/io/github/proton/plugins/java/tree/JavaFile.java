package io.github.proton.plugins.java.tree;

import io.vavr.collection.Vector;

public final class JavaFile {
    public final JavaPackageDeclaration packageDeclaration;
    public final Vector<JavaImportDeclaration> importDeclarations;

    public JavaFile(JavaPackageDeclaration packageDeclaration, Vector<JavaImportDeclaration> importDeclarations) {
        this.packageDeclaration = packageDeclaration;
        this.importDeclarations = importDeclarations;
    }
}
