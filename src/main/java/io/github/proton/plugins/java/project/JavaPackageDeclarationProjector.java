/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.project;

import io.github.proton.editor.Projection;
import io.github.proton.editor.Projector;
import io.github.proton.editor.TextProjection;
import io.github.proton.plugins.java.tree.JavaIdentifier;
import io.github.proton.plugins.java.tree.JavaPackageDeclaration;
import org.pf4j.Extension;

@Extension
public final class JavaPackageDeclarationProjector implements Projector<JavaPackageDeclaration> {
    @Override
    public Class<JavaPackageDeclaration> clazz() {
        return JavaPackageDeclaration.class;
    }

    @Override
    public Projection<JavaPackageDeclaration> project(JavaPackageDeclaration packageDeclaration) {
        Projection<JavaPackageDeclaration> label = TextProjection.label("package ", "keyword").of(packageDeclaration);
        Projection<JavaPackageDeclaration> projection = Projector.get(JavaIdentifier.class)
                .project(packageDeclaration.name)
                .map(JavaPackageDeclaration::new);
        return label.combine(projection);
    }
}
