package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.JavaName;
import io.vavr.control.Option;
import org.pf4j.Extension;

@Extension
public final class JavaNameProjector implements Projector<JavaName> {
    @Override
    public Class<JavaName> clazz() {
        return JavaName.class;
    }

    @Override
    public Projection<JavaName> project(JavaName name) {
        return TextProjection.text(new Text(name.chars()), "").mapChar(c -> c.modify(
            character -> JavaName.isValid(character)
                ? c.insert(character).map(JavaName::new)
                : Option.none(),
            () -> c.delete().map(JavaName::new)));
    }
}
