/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.project;

import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.github.proton.display.VectorProjection;
import io.github.proton.plugins.java.tree.JavaClassDeclaration;
import io.github.proton.plugins.java.tree.JavaFile;
import io.github.proton.plugins.java.tree.JavaImportDeclaration;
import io.github.proton.plugins.java.tree.JavaPackageDeclaration;
import io.github.proton.plugins.text.Line;
import io.vavr.collection.Vector;
import org.pf4j.Extension;

@Extension
public final class JavaFileProjector implements Projector<JavaFile> {
    @Override
    public Class<JavaFile> clazz() {
        return JavaFile.class;
    }

    @Override
    public Projection<JavaFile> project(JavaFile javaFile) {
        Projection<JavaFile> packageProjection = Projector.get(JavaPackageDeclaration.class)
                .project(javaFile.packageDeclaration)
                .map(packageDeclaration ->
                        new JavaFile(packageDeclaration, javaFile.importDeclarations, javaFile.classDeclarations));
        Projector<JavaImportDeclaration> importProjector = Projector.get(JavaImportDeclaration.class);
        Projection<JavaFile> importsProjection = new VectorProjection<>(
                        javaFile.importDeclarations, importProjector, new JavaImportDeclaration(new Line("")))
                .map(x -> new JavaFile(javaFile.packageDeclaration, x, javaFile.classDeclarations))
                .indentVertical(1);
        Projector<JavaClassDeclaration> classProjector = Projector.get(JavaClassDeclaration.class);
        Projection<JavaFile> classProjection = new VectorProjection<>(
                        javaFile.classDeclarations, classProjector, new JavaClassDeclaration(
                                new Line(""), Vector.empty()))
                .map(x -> new JavaFile(javaFile.packageDeclaration, javaFile.importDeclarations, x));
        return packageProjection.combineVertical(importsProjection).combineVertical(classProjection);
    }
}
