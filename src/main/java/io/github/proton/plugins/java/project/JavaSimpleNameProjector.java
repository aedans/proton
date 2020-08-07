package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.JavaSimpleName;
import io.vavr.control.Option;
import org.pf4j.Extension;

@Extension
public final class JavaSimpleNameProjector implements Projector<JavaSimpleName> {
    @Override
    public Class<JavaSimpleName> clazz() {
        return JavaSimpleName.class;
    }

    @Override
    public Projection<JavaSimpleName> project(JavaSimpleName name) {
        return TextProjection.text(new Text(name.chars()), "").mapChar(c -> c.modify(
            character -> JavaSimpleName.isValid(character)
                ? c.insert(character).map(JavaSimpleName::new)
                : Option.none(),
            () -> c.delete().map(JavaSimpleName::new)));
    }
}
