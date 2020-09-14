package io.github.proton.plugins.java.tree.type;

import com.github.javaparser.ast.type.ArrayType;
import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.JavaType;
import io.vavr.control.Option;

public record JavaArrayType(JavaType type) implements JavaType {
    public static JavaArrayType from(ArrayType array) {
        return new JavaArrayType(JavaType.from(array.getComponentType()));
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Projection<JavaType> projectType() {
        return type.project().map(JavaArrayType::new)
            .map(x -> (JavaType) x)
            .combine(TextProjection.label("[]", "punctuation.brace").of(this))
            .combine(Projection.trailing())
            .mapChar(c -> c.withDelete(() -> Option.some(c.delete().getOrElse(type))));
    }
}
