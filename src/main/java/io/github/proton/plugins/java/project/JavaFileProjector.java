package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
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
        var packageProjection = Projector.get(JavaPackageDeclaration.class)
            .project(javaFile.packageDeclaration())
            .map(packageDeclaration ->
                new JavaFile(packageDeclaration, javaFile.importDeclarations(), javaFile.classDeclarations()));
        var importsProjection = new VectorProjection<>(
            javaFile.importDeclarations(),
            Projector.get(JavaImportDeclaration.class)::project,
            new JavaImportDeclaration(new JavaIdentifier("")),
            Projection.newline(),
            JavaImportDeclaration::isEmpty,
            x -> x == '\n'
        ).map(x -> new JavaFile(javaFile.packageDeclaration(), x, javaFile.classDeclarations()));
        var classProjection = new VectorProjection<>(
            javaFile.classDeclarations(),
            Projector.get(JavaClassDeclaration.class)::project,
            new JavaClassDeclaration(new JavaIdentifier(""), Vector.empty()),
            Projection.newline(),
            JavaClassDeclaration::isEmpty,
            x -> false
        ).map(x -> new JavaFile(javaFile.packageDeclaration(), javaFile.importDeclarations(), x));
        return packageProjection
            .combine(Projection.trailingNewline())
            .combine(Projection.newline())
            .combine(importsProjection)
            .combine(Projection.trailingNewline())
            .combine(Projection.newline())
            .combine(classProjection);
    }
}
