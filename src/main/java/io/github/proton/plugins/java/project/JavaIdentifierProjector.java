package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.JavaIdentifier;
import io.vavr.control.Option;
import org.pf4j.Extension;

@Extension
public final class JavaIdentifierProjector implements Projector<JavaIdentifier> {
    @Override
    public Class<JavaIdentifier> clazz() {
        return JavaIdentifier.class;
    }

    @Override
    public Projection<JavaIdentifier> project(JavaIdentifier identifier) {
        return Projection.text(new Text(identifier.chars()), "").mapChars(c -> new Char<JavaIdentifier>() {
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
            public Option<JavaIdentifier> insert(char character) {
                if (JavaIdentifier.isValid(character)) {
                    return c.insert(character).map(JavaIdentifier::new);
                } else {
                    return Option.none();
                }
            }

            @Override
            public Option<JavaIdentifier> delete() {
                return c.delete().map(JavaIdentifier::new);
            }
        });
    }
}
