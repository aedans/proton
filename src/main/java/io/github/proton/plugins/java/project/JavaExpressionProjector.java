package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import io.github.proton.plugins.java.tree.expression.*;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import org.pf4j.Extension;

@Extension
public final class JavaExpressionProjector implements Projector<JavaExpression> {
    @Override
    public Class<JavaExpression> clazz() {
        return JavaExpression.class;
    }

    @Override
    public Projection<JavaExpression> project(JavaExpression expression) {
        if (expression instanceof JavaIdentifierExpression identifier) {
            return Projector.get(JavaIdentifier.class)
                .project(identifier.identifier())
                .map(JavaExpression::fromIdentifier)
                .mapChars(c -> binaryOp(c, identifier));
        } else if (expression instanceof JavaIntegerExpression i) {
            var s = Integer.toString(i.integer());
            return TextProjection.text(s, "constant.numeric")
                .map(JavaIdentifier::new)
                .mapChars(JavaExpressionProjector::identifierChar)
                .mapChars(c -> binaryOp(c, i));
        } else if (expression instanceof JavaBinaryExpression e) {
            var projection = Projector.get(JavaExpression.class);
            var left = projection.project(e.left())
                .map(l -> (JavaExpression) new JavaBinaryExpression(l, e.right(), e.op()))
                .mapChars(c -> c.withDelete(() -> Option.some(c.delete().getOrElse(e::left))));
            var right = projection.project(e.right())
                .map(r -> (JavaExpression) new JavaBinaryExpression(e.left(), r, e.op()));
            return left
                .combine(TextProjection.space.of(e))
                .combine(TextProjection.label(e.op().string, "").of(e))
                .combine(TextProjection.space.of(e))
                .combine(right);
        } else {
            throw new RuntimeException();
        }
    }

    public static Char<JavaExpression> identifierChar(Char<JavaIdentifier> c) {
        return c.modify(
            character -> JavaIdentifier.isValid(character)
                ? c.insert(character).map(JavaExpression::fromIdentifier)
                : Option.none(),
            () -> c.delete().map(JavaExpression::fromIdentifier));
    }

    public static Char<JavaExpression> binaryOp(Char<JavaExpression> c, JavaExpression expression) {
        return c.withInsert(character -> Vector.of(JavaBinaryExpression.Operator.values())
            .find(x -> x.string.equals(Character.toString(character)))
            .map(op -> Option.some((JavaExpression) new JavaBinaryExpression(
                expression,
                new JavaIdentifierExpression(""),
                op)))
            .getOrElse(c.insert(character)));
    }
}
