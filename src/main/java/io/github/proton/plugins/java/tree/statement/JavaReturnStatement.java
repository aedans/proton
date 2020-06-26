package io.github.proton.plugins.java.tree.statement;

import io.github.proton.plugins.java.tree.*;

public record JavaReturnStatement(JavaExpression expression) implements JavaStatement {
    @Override
    public boolean isEmpty() {
        return false;
    }
}
