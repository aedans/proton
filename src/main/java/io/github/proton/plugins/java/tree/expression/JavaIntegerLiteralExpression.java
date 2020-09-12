package io.github.proton.plugins.java.tree.expression;

import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;

public record JavaIntegerLiteralExpression(long l) implements JavaExpression {
    public static JavaIntegerLiteralExpression from(IntegerLiteralExpr expr) {
        return new JavaIntegerLiteralExpression(expr.asNumber().longValue());
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Projection<JavaExpression> project() {
        return new JavaSimpleName(Long.toString(l)).project()
            .mapChar(c -> c.withStyle("constant.numeric"))
            .map(name -> JavaExpression.from(name.toString()));
    }
}
