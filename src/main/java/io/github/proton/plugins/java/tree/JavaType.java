package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.type.*;
import io.github.proton.editor.Tree;
import io.vavr.collection.Vector;

public interface JavaType extends Tree<JavaType> {
    static JavaType from(Type type) {
        if (type instanceof ClassOrInterfaceType classOrInterfaceType) {
            return JavaClassOrInterfaceType.from(classOrInterfaceType);
        } else if (type instanceof PrimitiveType primitive) {
            return JavaPrimitiveType.from(primitive);
        } else {
            throw new IllegalArgumentException();
        }
    }

    static JavaType from(JavaSimpleName name) {
        if (Vector.of(JavaPrimitiveType.values()).exists(t -> new JavaSimpleName(t.name().toLowerCase()).equals(name))) {
            return JavaPrimitiveType.valueOf(name.toString().toUpperCase());
        } else {
            return new JavaClassOrInterfaceType(name);
        }
    }
}
