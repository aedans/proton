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
        return TextProjection.text(new Text(identifier.chars()), "").mapChars(c -> c.modify(
            character -> JavaIdentifier.isValid(character)
                ? c.insert(character).map(JavaIdentifier::new)
                : Option.none(),
            () -> c.delete().map(JavaIdentifier::new)));
    }
}
