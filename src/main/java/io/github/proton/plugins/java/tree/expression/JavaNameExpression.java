package io.github.proton.plugins.java.tree.expression;

import com.github.javaparser.ast.expr.NameExpr;
import io.github.proton.editor.Projection;
import io.github.proton.plugins.java.tree.*;

public record JavaNameExpression(JavaSimpleName name) implements JavaExpression {
    public static JavaNameExpression from(NameExpr name) {
        return new JavaNameExpression(JavaSimpleName.from(name.getName()));
    }

    @Override
    public boolean isEmpty() {
        return name.isEmpty();
    }

    @Override
    public Projection<JavaExpression> project() {
        return name.project().map(name -> JavaExpression.from(name.toString()));
    }
}
