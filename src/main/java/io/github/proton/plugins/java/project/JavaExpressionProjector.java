package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
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
        if (expression instanceof JavaExpression.Identifier identifier) {
            return Projector.get(JavaIdentifier.class)
                .project(identifier.identifier())
                .map(JavaExpression::fromIdentifier);
        } else if (expression instanceof JavaExpression.Int i) {
            var s = Integer.toString(i.integer());
            return TextProjection.text(s, "constant.numeric")
                .map(JavaIdentifier::new)
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
                });
        } else {
            throw new RuntimeException();
        }
    }
}
