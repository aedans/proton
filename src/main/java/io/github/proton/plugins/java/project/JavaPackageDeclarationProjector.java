/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.project;

import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.github.proton.plugins.java.tree.JavaIdentifier;
import io.github.proton.plugins.java.tree.JavaPackageDeclaration;
import io.github.proton.plugins.text.LabelProjection;
import org.pf4j.Extension;

@Extension
public final class JavaPackageDeclarationProjector implements Projector<JavaPackageDeclaration> {
    @Override
    public Class<JavaPackageDeclaration> clazz() {
        return JavaPackageDeclaration.class;
    }

    @Override
    public Projection<JavaPackageDeclaration> project(JavaPackageDeclaration packageDeclaration) {
        Projection<JavaPackageDeclaration> label = new LabelProjection("package ", "keyword").of(packageDeclaration);
        Projection<JavaPackageDeclaration> projection = Projector.get(JavaIdentifier.class)
                .project(packageDeclaration.name)
                .map(JavaPackageDeclaration::new);
        return label.combineHorizontal(projection);
    }
}
