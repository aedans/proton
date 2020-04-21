/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.project;

import io.github.proton.display.GroupProjection;
import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.github.proton.plugins.java.tree.JavaClassDeclaration;
import io.github.proton.plugins.java.tree.JavaFile;
import io.github.proton.plugins.java.tree.JavaImportDeclaration;
import io.github.proton.plugins.java.tree.JavaPackageDeclaration;
import io.github.proton.plugins.text.Line;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
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
        Projection<JavaFile> importsProjection = new GroupProjection<JavaFile, JavaImportDeclaration>() {
            @Override
            public Projection<JavaImportDeclaration> projectElem(JavaImportDeclaration elem) {
                return Projector.get(JavaImportDeclaration.class).project(elem);
            }

            @Override
            public Vector<JavaImportDeclaration> getElems() {
                return javaFile.importDeclarations;
            }

            @Override
            public JavaFile setElems(Vector<JavaImportDeclaration> elems) {
                return new JavaFile(javaFile.packageDeclaration, elems, javaFile.classDeclarations);
            }

            @Override
            public Option<JavaImportDeclaration> newElem() {
                return Option.some(new JavaImportDeclaration(new Line("")));
            }
        }.indentVertical(1);
        Projection<JavaFile> classProjection = new GroupProjection<JavaFile, JavaClassDeclaration>() {
            @Override
            public Projection<JavaClassDeclaration> projectElem(JavaClassDeclaration elem) {
                return Projector.get(JavaClassDeclaration.class).project(elem);
            }

            @Override
            public Vector<JavaClassDeclaration> getElems() {
                return javaFile.classDeclarations;
            }

            @Override
            public JavaFile setElems(Vector<JavaClassDeclaration> elems) {
                return new JavaFile(javaFile.packageDeclaration, javaFile.importDeclarations, elems);
            }

            @Override
            public Option<JavaClassDeclaration> newElem() {
                return Option.some(new JavaClassDeclaration(new Line(""), Vector.empty()));
            }
        }.indentVertical(1);
        return packageProjection.combineVertical(importsProjection).combineVertical(classProjection);
    }
}
