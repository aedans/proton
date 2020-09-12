package io.github.proton.plugins.java.tree.statement;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;

public record JavaExpressionStatement(JavaExpression expression) implements JavaStatement {
    @Override
    public boolean isEmpty() {
        return expression.isEmpty();
    }

    @Override
    public Projection<JavaStatement> project() {
        Projection<JavaStatement> expressionProjection = expression.project().map(JavaExpressionStatement::new);
        return expressionProjection.combine(TextProjection.semicolon.of(this));
    }
}
