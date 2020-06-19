/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.project;

import io.github.proton.editor.Projection;
import io.github.proton.editor.Projector;
import io.github.proton.editor.Style;
import io.github.proton.editor.StyledCharacter;
import io.github.proton.editor.Text;
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
        return Projector.get(Text.class).project(new Text(identifier.chars)).mapChars(c ->
                new Projection.Char<JavaIdentifier>() {
                    @Override
                    public boolean decorative() {
                        return c.decorative();
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
