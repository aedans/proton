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
                .mapChars(c -> binaryOpChar(c, i))
                .mapChars(c -> fieldChar(c, i))
                .mapChars(c -> c.withInsert(character -> character == '('
                    ? Option.some(new JavaFunctionExpression(i.identifier(), Vector.empty()))
                    : c.insert(character)));
        } else if (expression instanceof JavaIntegerExpression i) {
            var s = Integer.toString(i.integer());
            return TextProjection.text(s, "constant.numeric")
                .map(JavaIdentifier::new)
                .mapChars(JavaExpressionProjector::identifierChar)
                .mapChars(c -> binaryOpChar(c, i))
                .mapChars(c -> fieldChar(c, i));
        } else if (expression instanceof JavaBinaryExpression e) {
            var projection = Projector.get(JavaExpression.class);
            var left = projection.project(e.left())
                .map(l -> (JavaExpression) new JavaBinaryExpression(l, e.right(), e.op()))
                .mapChars(c -> c.withDelete(() -> Option.some(c.delete().getOrElse(e::left))));
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
                .mapChars(c -> c.withDelete(() -> c.delete().orElse(() -> f.args().isEmpty()
                    ? Option.some(new JavaIdentifierExpression(f.name()))
                    : Option.none())));
            var args = projectArgs(f.args())
                .map(as -> (JavaExpression) new JavaFunctionExpression(f.name(), as));
            return name
                .combine(args)
                .combine(Projection.chars(Char.empty(' ').withDecorative(false).withMerge(true))
                    .of((JavaExpression) f)
                    .mapChars(c -> binaryOpChar(c, f))
                    .mapChars(c -> fieldChar(c, f)));
        } else if (expression instanceof JavaFieldExpression f) {
            var expr = Projector.get(JavaExpression.class)
                .project(f.expr())
                .map(e -> (JavaExpression) new JavaFieldExpression(e, f.field()))
                .mapChars(c -> c.withDelete(() -> Option.some(c.delete().getOrElse(f::expr))));
            var field = Projector.get(JavaIdentifier.class)
                .project(f.field())
                .map(i -> (JavaExpression) new JavaFieldExpression(f.expr(), i))
                .mapChars(c -> binaryOpChar(c, f))
                .mapChars(c -> fieldChar(c, f))
                .mapChars(c -> c.withInsert(character -> character == '('
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
                .mapChars(c -> c.withDelete(() -> c.delete().orElse(() -> m.args().isEmpty()
                    ? Option.some(m.field())
                    : Option.none())));
            var args = projectArgs(m.args())
                .map(as -> (JavaExpression) new JavaMethodExpression(m.field(), as));
            return expr
                .combine(args)
                .combine(Projection.chars(Char.empty(' ').withDecorative(false).withMerge(true))
                    .of((JavaExpression) m)
                    .mapChars(c -> binaryOpChar(c, m))
                    .mapChars(c -> fieldChar(c, m)));
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
        Projector<JavaExpression> expressionProjector = Projector.get(JavaExpression.class);
        InsertProjection<JavaExpression> projection = new InsertProjection<>(
            args,
            t -> expressionProjector.project(t).group(),
            TextProjection.comma.combine(TextProjection.space).combine(Projection.linebreak())
                .of(Vector.empty()),
            new JavaIdentifierExpression(""),
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
