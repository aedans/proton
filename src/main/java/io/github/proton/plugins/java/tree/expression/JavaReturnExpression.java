package io.github.proton.plugins.java.tree.expression;

import io.github.proton.plugins.java.tree.JavaExpression;

public record JavaReturnExpression(JavaExpression expression) implements JavaExpression {
    @Override
    public boolean isEmpty() {
        return false;
    }
}
