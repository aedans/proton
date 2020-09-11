package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.body.VariableDeclarator;
import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.expression.JavaNameExpression;
import io.vavr.control.Option;

public record JavaVariableDeclarator(JavaSimpleName name,
                                     Option<JavaExpression> expression) implements Tree<JavaVariableDeclarator> {
    public JavaVariableDeclarator(JavaSimpleName name) {
        this(name, Option.none());
    }

    public static JavaVariableDeclarator from(VariableDeclarator variable) {
        return new JavaVariableDeclarator(
            JavaSimpleName.from(variable.getName()),
            Option.ofOptional(variable.getInitializer()).map(JavaExpression::from));
    }

    @Override
    public boolean isEmpty() {
        return name.isEmpty();
    }

    @Override
    public Projection<JavaVariableDeclarator> project() {
        var nameProjection = name.project().map(name -> new JavaVariableDeclarator(name, expression));
        return expression.map(expr -> {
            var projection = expr.isEmpty()
                ? nameProjection.mapChar(c -> c.withDelete(() -> Option.some(c.delete().getOrElse(new JavaVariableDeclarator(name)))))
                : nameProjection;
            return projection
                .combine(TextProjection.space.of(this))
                .combine(TextProjection.equals.of(this))
                .combine(TextProjection.space.of(this))
                .combine(expr.project()
                    .map(expression -> new JavaVariableDeclarator(name, Option.some(expression))));
        }).getOrElse(() -> nameProjection.mapChar(c -> c.withInsert(character ->
            character == '='
                ? Option.some(new JavaVariableDeclarator(name, Option.some(new JavaNameExpression(new JavaSimpleName("")))))
                : c.insert(character))));
    }
}
