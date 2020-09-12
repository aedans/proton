package io.github.proton.plugins.java.tree.statement;

import com.github.javaparser.ast.stmt.ReturnStmt;
import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import io.github.proton.plugins.java.tree.expression.JavaNameExpression;
import io.vavr.control.Option;

public record JavaReturnStatement(Option<JavaExpression> expression) implements JavaStatement {
    public static JavaReturnStatement from(ReturnStmt stmt) {
        return new JavaReturnStatement(Option.ofOptional(stmt.getExpression()).map(JavaExpression::from));
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Projection<JavaStatement> project() {
        Projection<JavaStatement> label = new JavaSimpleName("return").project()
            .mapChar(c -> c.withStyle("keyword"))
            .map(name -> JavaStatement.from(name.toString()));
        return expression.map(expr -> {
            Projection<JavaStatement> expressionProjection = expr.project().map(expression -> new JavaReturnStatement(Option.some(expression)));
            return label.combine(TextProjection.space.of(this)).combine(expressionProjection);
        }).getOrElse(() ->
            label.mapChar(c -> c.withInsert(character -> character == ' '
                ? Option.some(new JavaReturnStatement(Option.some(new JavaNameExpression(new JavaSimpleName("")))))
                : c.insert(character))))
            .combine(TextProjection.semicolon.of(this));
    }
}
