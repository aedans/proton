package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.expr.*;
import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.expression.*;

public interface JavaExpression extends Tree<JavaExpression> {
    static JavaExpression from(Expression expression) {
        if (expression instanceof NameExpr name) {
            return JavaNameExpression.from(name);
        } else if (expression instanceof IntegerLiteralExpr integer) {
            return JavaIntegerLiteralExpression.from(integer);
        } else {
            return new JavaExpression() {
                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public Projection<JavaExpression> project() {
                    return TextProjection.text(expression.getClass().getSimpleName(), "invalid.illegal").of(this);
                }
            };
        }
    }

    static JavaExpression from(String string) {
        try {
            return new JavaIntegerLiteralExpression(Long.parseLong(string));
        } catch (NumberFormatException e) {
            return new JavaNameExpression(new JavaSimpleName(string));
        }
    }
}
