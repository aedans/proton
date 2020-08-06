package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import io.vavr.collection.Vector;
import org.pf4j.Extension;

@Extension
public final class JavaCompilationUnitProjector implements Projector<JavaCompilationUnit> {
    @Override
    public Class<JavaCompilationUnit> clazz() {
        return JavaCompilationUnit.class;
    }

    @Override
    public Projection<JavaCompilationUnit> project(JavaCompilationUnit compilationUnit) {
        var packageProjection = new OptionProjection<>(
            compilationUnit.packageDeclaration(),
            Projector.get(JavaPackage.class)::project,
            new JavaPackage(new JavaQualifiedIdentifier(Vector.empty())),
            JavaPackage::isEmpty
        ).map(x -> new JavaCompilationUnit(x, compilationUnit.importDeclarations(), compilationUnit.classDeclarations()));
        var importsProjection = new VectorProjection<>(
            compilationUnit.importDeclarations(),
            Projector.get(JavaImport.class)::project,
            new JavaImport(new JavaQualifiedIdentifier(Vector.empty()), false),
            Projection.newline(),
            JavaImport::isEmpty,
            x -> x == '\n'
        ).map(x -> new JavaCompilationUnit(compilationUnit.packageDeclaration(), x, compilationUnit.classDeclarations()));
        var classesProjection = new VectorProjection<>(
            compilationUnit.classDeclarations(),
            Projector.get(JavaClass.class)::project,
            new JavaClass(new JavaName("")),
            Projection.trailingNewline().combine(Projection.newline()),
            JavaClass::isEmpty,
            x -> false
        ).map(x -> new JavaCompilationUnit(compilationUnit.packageDeclaration(), compilationUnit.importDeclarations(), x));
        return packageProjection
            .combine(Projection.trailingNewline())
            .combine(Projection.newline())
            .combine(importsProjection)
            .combine(Projection.trailingNewline())
            .combine(Projection.newline())
            .combine(classesProjection);
    }
}
