package io.github.proton.plugins.java.tree.expression;

import io.github.proton.plugins.java.tree.JavaExpression;

public record JavaIntegerExpression(int integer) implements JavaExpression {
    @Override
    public boolean isEmpty() {
        return false;
    }
}
