package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.stmt.*;
import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.statement.*;
import io.vavr.control.Option;

public interface JavaStatement extends Tree<JavaStatement> {
    static JavaStatement from(Statement statement) {
        if (statement instanceof ReturnStmt stmt) {
            return JavaReturnStatement.from(stmt);
        } else {
            return new JavaStatement() {
                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public Projection<JavaStatement> project() {
                    return TextProjection.text(statement.getClass().getSimpleName(), "invalid.illegal").of(this);
                }
            };
        }
    }

    static JavaStatement from(String string) {
        if (string.equals("return")) {
            return new JavaReturnStatement(Option.none());
        } else {
            return new JavaNameStatement(new JavaSimpleName(string));
        }
    }
}
