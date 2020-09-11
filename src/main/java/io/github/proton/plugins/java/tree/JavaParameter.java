package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.body.Parameter;
import io.github.proton.editor.*;

public record JavaParameter(JavaType type, JavaSimpleName name) implements Tree<JavaParameter> {
    public static JavaParameter from(Parameter parameter) {
        return new JavaParameter(
            JavaType.from(parameter.getType()),
            JavaSimpleName.from(parameter.getName())
        );
    }

    @Override
    public boolean isEmpty() {
        return type.isEmpty() && name.isEmpty();
    }

    @Override
    public Projection<JavaParameter> project() {
        var typeProjection = type.project().map(type -> new JavaParameter(type, name));
        var nameProjection = name.project().map(name -> new JavaParameter(type, name));
        if (isEmpty()) {
            return typeProjection;
        } else {
            return typeProjection.combine(TextProjection.space.of(this)).combine(nameProjection);
        }
    }
}
