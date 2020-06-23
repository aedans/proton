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
        var importProjector = Projector.get(JavaImportDeclaration.class);
        var importsProjection = new VectorProjection<>(
            javaFile.importDeclarations(),
            importProjector,
            Projection.newline(),
            new JavaImportDeclaration(new JavaIdentifier("")),
            JavaImportDeclaration::isEmpty
        ).map(x -> new JavaFile(javaFile.packageDeclaration(), x, javaFile.classDeclarations()));
        var classProjector = Projector.get(JavaClassDeclaration.class);
        var classProjection = new VectorProjection<>(
            javaFile.classDeclarations(),
            classProjector,
            Projection.newline(),
            new JavaClassDeclaration(new JavaIdentifier(""), Vector.empty()),
            JavaClassDeclaration::isEmpty
        ).map(x -> new JavaFile(javaFile.packageDeclaration(), javaFile.importDeclarations(), x));
        return packageProjection
            .combine(Projection.newline())
            .combine(Projection.newline())
            .combine(importsProjection)
            .combine(Projection.newline())
            .combine(Projection.newline())
            .combine(classProjection);
    }
}
