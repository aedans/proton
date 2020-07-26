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
        if (expression instanceof JavaIdentifierExpression i) {
            return Projector.get(JavaIdentifier.class)
                .project(i.identifier())
                .map(JavaExpression::fromIdentifier)
                .mapChar(c -> binaryOpChar(c, i))
                .mapChar(c -> fieldChar(c, i))
                .mapChar(c -> c.withInsert(character -> character == '('
                    ? Option.some(new JavaFunctionExpression(i.identifier(), Vector.empty()))
                    : c.insert(character)));
        } else if (expression instanceof JavaIntegerExpression i) {
            var s = Integer.toString(i.integer());
            return TextProjection.text(s, "constant.numeric")
                .map(JavaIdentifier::new)
                .mapChar(JavaExpressionProjector::identifierChar)
                .mapChar(c -> binaryOpChar(c, i))
                .mapChar(c -> fieldChar(c, i));
        } else if (expression instanceof JavaBinaryExpression e) {
            var projection = Projector.get(JavaExpression.class);
            var left = projection.project(e.left())
                .map(l -> (JavaExpression) new JavaBinaryExpression(l, e.right(), e.op()))
                .mapChar(c -> c.withDelete(() -> Option.some(c.delete().getOrElse(e::left))));
            var right = projection.project(e.right())
                .map(r -> (JavaExpression) new JavaBinaryExpression(e.left(), r, e.op()));
            return left
                .combine(TextProjection.space.of(e))
                .combine(Projection.linebreak())
                .combine(TextProjection.label(e.op().string, "").of(e))
                .combine(TextProjection.space.of(e))
                .combine(right);
        } else if (expression instanceof JavaFunctionExpression f) {
            var name = Projector.get(JavaIdentifier.class)
                .project(f.name())
                .map(i -> (JavaExpression) new JavaFunctionExpression(i, f.args()))
                .mapChar(c -> c.withDelete(() -> c.delete().orElse(() -> f.args().isEmpty()
                    ? Option.some(new JavaIdentifierExpression(f.name()))
                    : Option.none())));
            var args = projectArgs(f.args())
                .map(as -> (JavaExpression) new JavaFunctionExpression(f.name(), as));
            return name
                .combine(args)
                .combine(Projection.trailing()
                    .of((JavaExpression) f)
                    .mapChar(c -> binaryOpChar(c, f))
                    .mapChar(c -> fieldChar(c, f)));
        } else if (expression instanceof JavaFieldExpression f) {
            var expr = Projector.get(JavaExpression.class)
                .project(f.expr())
                .map(e -> (JavaExpression) new JavaFieldExpression(e, f.field()))
                .mapChar(c -> c.withDelete(() -> Option.some(c.delete().getOrElse(f::expr))));
            var field = Projector.get(JavaIdentifier.class)
                .project(f.field())
                .map(i -> (JavaExpression) new JavaFieldExpression(f.expr(), i))
                .mapChar(c -> binaryOpChar(c, f))
                .mapChar(c -> fieldChar(c, f))
                .mapChar(c -> c.withInsert(character -> character == '('
                    ? Option.some(new JavaMethodExpression(f, Vector.empty()))
                    : c.insert(character)));
            return expr
                .combine(Projection.linebreak())
                .combine(TextProjection.dot.of(f))
                .combine(field);
        } else if (expression instanceof JavaMethodExpression m) {
            var expr = Projector.get(JavaExpression.class)
                .project(m.field())
                .map(e -> e instanceof JavaFieldExpression f ? new JavaMethodExpression(f, m.args()) : e)
                .mapChar(c -> c.withDelete(() -> c.delete().orElse(() -> m.args().isEmpty()
                    ? Option.some(m.field())
                    : Option.none())));
            var args = projectArgs(m.args())
                .map(as -> (JavaExpression) new JavaMethodExpression(m.field(), as));
            return expr
                .combine(args)
                .combine(Projection.trailing()
                    .of((JavaExpression) m)
                    .mapChar(c -> binaryOpChar(c, m))
                    .mapChar(c -> fieldChar(c, m)));
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

    public static Char<JavaExpression> binaryOpChar(Char<JavaExpression> c, JavaExpression expression) {
        return c.withInsert(character -> Vector.of(JavaBinaryExpression.Operator.values())
            .find(x -> x.string.equals(Character.toString(character)))
            .map(op -> Option.some((JavaExpression) new JavaBinaryExpression(
                expression,
                new JavaIdentifierExpression(""),
                op)))
            .getOrElse(c.insert(character)));
    }

    public static Char<JavaExpression> fieldChar(Char<JavaExpression> c, JavaExpression expression) {
        return c.withInsert(character -> character == '.'
            ? Option.some(new JavaFieldExpression(expression, new JavaIdentifier("")))
            : c.insert(character));
    }

    public static Projection<Vector<JavaExpression>> projectArgs(Vector<JavaExpression> args) {
        var expressionProjector = Projector.get(JavaExpression.class);
        VectorProjection<JavaExpression> projection = new VectorProjection<>(
            args,
            t -> expressionProjector.project(t).indent(2).group(),
            new JavaIdentifierExpression(""),
            TextProjection.comma.combine(TextProjection.space).combine(Projection.linebreak()).of(Vector.empty()),
            JavaExpression::isEmpty,
            c -> c == ',');
        return TextProjection.openParen.of(args)
            .combine(Projection.linebreak())
            .combine(projection)
            .combine(TextProjection.closeParen.of(args))
            .group()
            .indent(2);
    }
}
