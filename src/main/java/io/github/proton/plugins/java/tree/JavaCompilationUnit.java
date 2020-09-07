package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.CompilationUnit;
import io.github.proton.editor.*;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

public record JavaCompilationUnit(Option<JavaPackage> packageDeclaration,
                                  Vector<JavaImport> importDeclarations,
                                  Vector<JavaClass> classDeclarations) implements Tree<JavaCompilationUnit> {
    public static JavaCompilationUnit from(CompilationUnit tree) {
        return new JavaCompilationUnit(
            Option.ofOptional(tree.getPackageDeclaration()).map(JavaPackage::from),
            Vector.ofAll(tree.getImports()).map(JavaImport::from),
            Vector.ofAll(tree.getTypes()).map(JavaClass::from)
        );
    }

    @Override
    public Projection<JavaCompilationUnit> project() {
        var packageProjection = new OptionProjection<>(
            packageDeclaration,
            new JavaPackage(new JavaName(Vector.empty())),
            JavaPackage::isEmpty
        ).map(x -> new JavaCompilationUnit(x, importDeclarations, classDeclarations));
        var importsProjection = new VectorProjection<>(
            importDeclarations,
            new JavaImport(new JavaName(Vector.empty()), false),
            Projection.newline(),
            JavaImport::isEmpty,
            x -> x == '\n'
        ).map(x -> new JavaCompilationUnit(packageDeclaration, x, classDeclarations));
        var classesProjection = new VectorProjection<>(
            classDeclarations,
            new JavaClass(new JavaSimpleName("")),
            Projection.trailingNewline().combine(Projection.newline()),
            JavaClass::isEmpty,
            x -> false
        ).map(x -> new JavaCompilationUnit(packageDeclaration, importDeclarations, x));
        return packageProjection
            .combine(Projection.trailingNewline())
            .combine(Projection.newline())
            .combine(importsProjection)
            .combine(Projection.trailingNewline())
            .combine(Projection.newline())
            .combine(classesProjection);
    }
}
