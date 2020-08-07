package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.CompilationUnit;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

public record JavaCompilationUnit(Option<JavaPackage> packageDeclaration,
                                  Vector<JavaImport> importDeclarations,
                                  Vector<JavaClass> classDeclarations) {
    public static JavaCompilationUnit from(CompilationUnit tree) {
        return new JavaCompilationUnit(
            Option.ofOptional(tree.getPackageDeclaration()).map(JavaPackage::from),
            Vector.ofAll(tree.getImports()).map(JavaImport::from),
            Vector.ofAll(tree.getTypes()).map(JavaClass::from)
        );
    }
}
