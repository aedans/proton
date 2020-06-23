package io.github.proton.plugins.java.tree;

import io.github.proton.plugins.java.tree.expression.*;

public interface JavaExpression {
    static JavaExpression fromIdentifier(JavaIdentifier identifier) {
        if (identifier.toString().equals("return")) {
            return new JavaReturnExpression(new JavaIdentifierExpression(""));
        }
        try {
            return new JavaIntegerExpression(Integer.parseInt(identifier.toString()));
        } catch (NumberFormatException ignored) {
        }
        return new JavaIdentifierExpression(identifier);
    }

    boolean isEmpty();
}
