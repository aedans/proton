package io.github.proton.plugins.java.tree.statement;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;

public record JavaNameStatement(JavaSimpleName name) implements JavaStatement {
    @Override
    public boolean isEmpty() {
        return name.isEmpty();
    }

    @Override
    public Projection<JavaStatement> project() {
        return name.project().map(name -> JavaStatement.from(name.toString()))
            .combine(TextProjection.semicolon.of(this));
    }
}
