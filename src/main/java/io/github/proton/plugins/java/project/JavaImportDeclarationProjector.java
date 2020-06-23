package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import org.pf4j.Extension;

@Extension
public final class JavaImportDeclarationProjector implements Projector<JavaImportDeclaration> {
    @Override
    public Class<JavaImportDeclaration> clazz() {
        return JavaImportDeclaration.class;
    }

    @Override
    public Projection<JavaImportDeclaration> project(JavaImportDeclaration importDeclaration) {
        var label = TextProjection.label("import", "keyword").of(importDeclaration);
        var projection = Projector.get(JavaIdentifier.class)
            .project(importDeclaration.name())
            .map(JavaImportDeclaration::new);
        return label
            .combine(TextProjection.space.of(importDeclaration))
            .combine(projection);
    }
}
