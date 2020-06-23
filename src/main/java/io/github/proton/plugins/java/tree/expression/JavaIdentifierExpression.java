package io.github.proton.plugins.java.tree.expression;

import io.github.proton.plugins.java.tree.*;

public record JavaIdentifierExpression(JavaIdentifier identifier) implements JavaExpression {
    public JavaIdentifierExpression(String s) {
        this(new JavaIdentifier(s));
    }

    @Override
    public boolean isEmpty() {
        return identifier.isEmpty();
    }
}
