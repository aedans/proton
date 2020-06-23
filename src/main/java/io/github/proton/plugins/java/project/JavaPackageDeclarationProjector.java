package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import org.pf4j.Extension;

@Extension
public final class JavaPackageDeclarationProjector implements Projector<JavaPackageDeclaration> {
    @Override
    public Class<JavaPackageDeclaration> clazz() {
        return JavaPackageDeclaration.class;
    }

    @Override
    public Projection<JavaPackageDeclaration> project(JavaPackageDeclaration packageDeclaration) {
        var label = Projection.label("package", "keyword").of(packageDeclaration);
        var projection = Projector.get(JavaIdentifier.class)
            .project(packageDeclaration.name())
            .map(JavaPackageDeclaration::new);
        return label.combine(projection);
    }
}
