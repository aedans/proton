package io.github.proton.plugins.java.tree.declaration;

import com.github.javaparser.ast.body.FieldDeclaration;
import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

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
        Projection<JavaBodyDeclaration> typeProjection = type.project().map(type -> new JavaFieldDeclaration(type, variables));
        Projection<JavaBodyDeclaration> variablesProjection = new VectorProjection<>(
            variables,
            new JavaVariableDeclarator(new JavaSimpleName("")),
            TextProjection.comma.combine(TextProjection.space),
            x -> x == ','
        ).map(variables -> new JavaFieldDeclaration(type, variables));
        if (variables.size() == 1) {
            variablesProjection = variablesProjection.mapChar(c ->
                c.withInsert(character -> character == '('
                    ? Option.some(new JavaMethodDeclaration(type, variables.get().name(), Vector.empty()))
                    : c.insert(character)));
        }
        return typeProjection
            .combine(TextProjection.space.of(this))
            .combine(variablesProjection)
            .combine(TextProjection.semicolon.of(this))
            .map(x -> x);
    }
}
