package io.github.proton.plugins.java.tree;

import io.github.proton.plugins.java.tree.expression.*;

public interface JavaExpression extends JavaStatement {
    static JavaExpression fromIdentifier(JavaIdentifier identifier) {
        try {
            return new JavaIntegerExpression(Integer.parseInt(identifier.toString()));
        } catch (NumberFormatException ignored) {
        }
        return new JavaIdentifierExpression(identifier);
    }
}
