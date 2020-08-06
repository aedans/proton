package io.github.proton.plugins.java.tree;

import com.sun.source.tree.CompilationUnitTree;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

public record JavaCompilationUnit(Option<JavaPackage> packageDeclaration,
                                  Vector<JavaImport> importDeclarations,
                                  Vector<JavaClass> classDeclarations) {
    public static JavaCompilationUnit from(CompilationUnitTree tree) {
        return new JavaCompilationUnit(
            Option.of(tree.getPackage()).map(JavaPackage::from),
            Vector.ofAll(tree.getImports()).map(JavaImport::from),
            Vector.ofAll(tree.getTypeDecls()).map(JavaClass::from)
        );
    }
}
