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
        Projector<JavaImportDeclaration> importProjector = Projector.get(JavaImportDeclaration.class);
        var importsProjection = new AppendProjection<>(
            javaFile.importDeclarations(),
            x -> Projection.<JavaImportDeclaration>newline().combine(importProjector.project(x)),
            new JavaImportDeclaration(new JavaIdentifier("")),
            JavaImportDeclaration::isEmpty
        ).map(x -> new JavaFile(javaFile.packageDeclaration(), x, javaFile.classDeclarations()));
        Projector<JavaClassDeclaration> classProjector = Projector.get(JavaClassDeclaration.class);
        var classProjection = new AppendProjection<>(
            javaFile.classDeclarations(),
            x -> Projection.<JavaClassDeclaration>newline().combine(classProjector.project(x)),
            new JavaClassDeclaration(new JavaIdentifier(""), Vector.empty()),
            JavaClassDeclaration::isEmpty
        ).map(x -> new JavaFile(javaFile.packageDeclaration(), javaFile.importDeclarations(), x));
        return packageProjection
            .combine(Projection.trailingNewline())
            .combine(importsProjection)
            .combine(Projection.trailingNewline())
            .combine(classProjection);
    }
}
