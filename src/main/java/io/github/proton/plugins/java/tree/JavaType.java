package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.type.*;
import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.type.*;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

public interface JavaType extends Tree<JavaType> {
    Projection<JavaType> projectType();

    @Override
    default Projection<JavaType> project() {
        return projectType()
            .mapChar(c -> c.withInsert(character ->
                character == '['
                    ? Option.some(new JavaArrayType(this))
                    : c.insert(character)));
    }

    static JavaType from(Type type) {
        if (type instanceof ClassOrInterfaceType classOrInterfaceType) {
            return JavaClassOrInterfaceType.from(classOrInterfaceType);
        } else if (type instanceof PrimitiveType primitive) {
            return JavaPrimitiveType.from(primitive);
        } else if (type instanceof VoidType) {
            return JavaPrimitiveType.VOID;
        } else if (type instanceof VarType) {
            return JavaPrimitiveType.VAR;
        } else if (type instanceof ArrayType array) {
            return JavaArrayType.from(array);
        } else {
            return new JavaType() {
                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public Projection<JavaType> projectType() {
                    return TextProjection.text(type.getClass().getSimpleName(), "invalid.illegal").of(this);
                }
            };
        }
    }

    static JavaType from(String string) {
        if (Vector.of(JavaPrimitiveType.values()).exists(t -> t.name().toLowerCase().equals(string))) {
            return JavaPrimitiveType.valueOf(string.toUpperCase());
        } else {
            return new JavaClassOrInterfaceType(new JavaSimpleName(string));
        }
    }
}
