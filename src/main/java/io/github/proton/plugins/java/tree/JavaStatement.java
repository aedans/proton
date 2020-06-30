package io.github.proton.plugins.java.tree;

import io.github.proton.plugins.java.tree.expression.JavaIdentifierExpression;
import io.github.proton.plugins.java.tree.statement.*;
import io.vavr.collection.Vector;

public interface JavaStatement {
    static JavaStatement fromIdentifier(JavaIdentifier identifier) {
        if (identifier.toString().equals("return")) {
            return new JavaReturnStatement(new JavaIdentifierExpression(""));
        } else if (identifier.toString().equals("if")) {
            return new JavaIfStatement(new JavaIdentifierExpression(""), Vector.empty(), Vector.empty());
        } else {
            return JavaExpression.fromIdentifier(identifier);
        }
    }

    default boolean isEmpty() {
        return false;
    }
}
