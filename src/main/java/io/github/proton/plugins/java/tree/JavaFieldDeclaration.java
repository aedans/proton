package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.body.FieldDeclaration;
import io.github.proton.editor.*;
import io.vavr.collection.Vector;

public record JavaFieldDeclaration(JavaType type, Vector<JavaVariableDeclarator> variables) implements Tree<JavaBodyDeclaration>, JavaBodyDeclaration {
    public static JavaFieldDeclaration from(FieldDeclaration field) {
        return new JavaFieldDeclaration(
            JavaType.from(field.getVariable(0).getType()),
            Vector.ofAll(field.getVariables()).map(JavaVariableDeclarator::from)
        );
    }

    @Override
    public boolean isEmpty() {
        return type.isEmpty() && variables.isEmpty();
    }

    @Override
    public Projection<JavaBodyDeclaration> project() {
        var typeProjection = type.project().map(type -> new JavaFieldDeclaration(type, variables));
        var variablesProjection = new VectorProjection<>(
            variables,
            new JavaVariableDeclarator(new JavaSimpleName("")),
            TextProjection.comma.combine(TextProjection.space),
            x -> x == ','
        ).map(variables -> new JavaFieldDeclaration(type, variables));
        return typeProjection
            .combine(TextProjection.space.of(this))
            .combine(variablesProjection)
            .combine(TextProjection.semicolon.of(this))
            .map(x -> x);
    }
}
