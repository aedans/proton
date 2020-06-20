/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.project;

import io.github.proton.editor.Projection;
import io.github.proton.editor.Projector;
import io.github.proton.plugins.java.tree.JavaIdentifier;
import io.github.proton.plugins.java.tree.JavaImportDeclaration;
import org.pf4j.Extension;

@Extension
public final class JavaImportDeclarationProjector implements Projector<JavaImportDeclaration> {
    @Override
    public Class<JavaImportDeclaration> clazz() {
        return JavaImportDeclaration.class;
    }

    @Override
    public Projection<JavaImportDeclaration> project(JavaImportDeclaration importDeclaration) {
        Projection<JavaImportDeclaration> label = Projection.label("import", "keyword").of(importDeclaration);
        Projection<JavaImportDeclaration> projection = Projector.get(JavaIdentifier.class)
                .project(importDeclaration.name)
                .map(JavaImportDeclaration::new);
        return label.combine(projection);
    }
}
