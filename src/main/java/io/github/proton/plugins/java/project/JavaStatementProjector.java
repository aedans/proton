package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import io.github.proton.plugins.java.tree.expression.JavaIdentifierExpression;
import io.github.proton.plugins.java.tree.statement.*;
import org.pf4j.Extension;

@Extension
public final class JavaStatementProjector implements Projector<JavaStatement> {
    @Override
    public Class<JavaStatement> clazz() {
        return JavaStatement.class;
    }

    @Override
    public Projection<JavaStatement> project(JavaStatement statement) {
        if (statement instanceof JavaIdentifierExpression identifier) {
            return Projector.get(JavaExpression.class)
                .project(identifier)
                .map(expression -> expression instanceof JavaIdentifierExpression i
                    ? JavaStatement.fromIdentifier(i.identifier())
                    : expression);
        } else if (statement instanceof JavaReturnStatement r) {
            var projection = Projector.get(JavaExpression.class)
                .project(r.expression())
                .map(x -> (JavaStatement) new JavaReturnStatement(x));
            var ret = TextProjection.text("return", "keyword")
                .map(JavaIdentifier::new)
                .mapChar(JavaExpressionProjector::identifierChar)
                .map(x -> (JavaStatement) x);
            return ret
                .combine(TextProjection.space.of(r))
                .combine(projection)
                .map(x -> x);
        } else if (statement instanceof JavaIfStatement f) {
            var condition = Projector.get(JavaExpression.class)
                .project(f.condition())
                .map(c -> (JavaStatement) new JavaIfStatement(c, f.trueBlock(), f.falseBlock()));
            var statementProjector = Projector.get(JavaStatement.class);
            var trueBlock = new VectorProjection<>(
                f.trueBlock(),
                statementProjector::project,
                new JavaIdentifierExpression(""),
                Projection.newline(),
                JavaStatement::isEmpty,
                x -> x == '\n'
            ).indent(2)
                .withAppend(Projection.<Text>newline().combine(TextProjection.closeBracket))
                .map(ts -> new JavaIfStatement(f.condition(), ts, f.falseBlock()));
            var falseBlock = new VectorProjection<>(
                f.falseBlock(),
                statementProjector::project,
                new JavaIdentifierExpression(""),
                Projection.newline(),
                JavaStatement::isEmpty,
                x -> x == '\n'
            ).indent(2)
                .withAppend(Projection.<Text>newline().combine(TextProjection.closeBracket))
                .map(fs -> new JavaIfStatement(f.condition(), f.trueBlock(), fs));
            var iff = TextProjection.text("if", "keyword")
                .map(JavaIdentifier::new)
                .mapChar(JavaExpressionProjector::identifierChar)
                .map(x -> (JavaStatement) x);
            return iff
                .combine(TextProjection.space.of(f))
                .combine(condition.group())
                .combine(TextProjection.space.of(f))
                .combine(TextProjection.openBracket.of(f))
                .combine(Projection.<JavaStatement>newline().indent(2).combine(trueBlock.map(x -> x)))
                .combine(TextProjection.space.of(f))
                .combine(TextProjection.label("else", "keyword").of(f))
                .combine(TextProjection.space.of(f))
                .combine(TextProjection.openBracket.of(f))
                .combine(Projection.<JavaStatement>newline().indent(2).combine(falseBlock.map(x -> x)));
        } else if (statement instanceof JavaExpression expression) {
            return Projector.get(JavaExpression.class).project(expression).map(x -> x);
        } else {
            throw new RuntimeException();
        }
    }
}
