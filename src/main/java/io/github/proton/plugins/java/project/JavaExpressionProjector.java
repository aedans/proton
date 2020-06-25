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
                .mapChars(this::identifierChar)
                .mapChars(c -> binaryOp(c, i));
        } else if (expression instanceof JavaReturnExpression r) {
            var projection = Projector.get(JavaExpression.class)
                .project(r.expression())
                .map(x -> (JavaExpression) new JavaReturnExpression(x));
            var ret = TextProjection.text("return", "keyword")
                .map(JavaIdentifier::new)
                .mapChars(this::identifierChar);
            return ret
                .combine(TextProjection.space.of(r))
                .combine(projection)
                .map(x -> x);
        } else if (expression instanceof JavaBinaryExpression e) {
            var projection = Projector.get(JavaExpression.class);
            var left = projection.project(e.left())
                .map(l -> (JavaExpression) new JavaBinaryExpression(l, e.right(), e.op()))
                .mapChars(c -> new Char<JavaExpression>() {
                    @Override
                    public boolean decorative() {
                        return c.decorative();
                    }

                    @Override
                    public boolean mergeable() {
                        return c.mergeable();
                    }

                    @Override
                    public StyledCharacter character(Style style) {
                        return c.character(style);
                    }

                    @Override
                    public Option<JavaExpression> insert(char character) {
                        return c.insert(character);
                    }

                    @Override
                    public Option<JavaExpression> delete() {
                        return Option.some(c.delete().getOrElse(e::left));
                    }
                });
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

    private Char<JavaExpression> identifierChar(Char<JavaIdentifier> c) {
        return new Char<JavaExpression>() {
            @Override
            public boolean decorative() {
                return c.decorative();
            }

            @Override
            public boolean mergeable() {
                return c.mergeable();
            }

            @Override
            public StyledCharacter character(Style style) {
                return c.character(style);
            }

            @Override
            public Option<JavaExpression> insert(char character) {
                if (JavaIdentifier.isValid(character)) {
                    return c.insert(character).map(JavaExpression::fromIdentifier);
                } else {
                    return Option.none();
                }
            }

            @Override
            public Option<JavaExpression> delete() {
                return c.delete().map(JavaExpression::fromIdentifier);
            }
        };
    }

    private Char<JavaExpression> binaryOp(Char<JavaExpression> c, JavaExpression expression) {
        return new Char<JavaExpression>() {
            @Override
            public boolean decorative() {
                return c.decorative();
            }

            @Override
            public boolean mergeable() {
                return c.mergeable();
            }

            @Override
            public StyledCharacter character(Style style) {
                return c.character(style);
            }

            @Override
            public Option<JavaExpression> insert(char character) {
                return Vector.of(JavaBinaryExpression.Operator.values())
                    .find(x -> x.string.equals(Character.toString(character)))
                    .map(op -> Option.some((JavaExpression) new JavaBinaryExpression(
                        expression,
                        new JavaIdentifierExpression(""),
                        op)))
                    .getOrElse(c.insert(character));
            }

            @Override
            public Option<JavaExpression> delete() {
                return c.delete();
            }
        };
    }
}
