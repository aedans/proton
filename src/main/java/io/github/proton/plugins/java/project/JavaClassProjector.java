package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import org.pf4j.Extension;

@Extension
public final class JavaClassProjector implements Projector<JavaClass> {
    @Override
    public Class<JavaClass> clazz() {
        return JavaClass.class;
    }

    @Override
    public Projection<JavaClass> project(JavaClass javaClass) {
        var label = TextProjection.label("class", "keyword").of(javaClass);
        var name = Projector.get(JavaName.class)
            .project(javaClass.name())
            .map(JavaClass::new);
        return label
            .combine(TextProjection.space.of(javaClass))
            .combine(name)
            .combine(TextProjection.space.of(javaClass))
            .combine(TextProjection.openBracket.of(javaClass))
            .combine(TextProjection.closeBracket.of(javaClass));
    }
}
