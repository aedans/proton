/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.project;

import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.github.proton.plugins.java.tree.JavaImportDeclaration;
import io.github.proton.plugins.text.LabelProjection;
import io.github.proton.plugins.text.Line;
import org.pf4j.Extension;

@Extension
public final class JavaImportDeclarationProjector implements Projector<JavaImportDeclaration> {
    @Override
    public Class<JavaImportDeclaration> clazz() {
        return JavaImportDeclaration.class;
    }

    @Override
    public Projection<JavaImportDeclaration> project(JavaImportDeclaration importDeclaration) {
        Projection<JavaImportDeclaration> label = new LabelProjection("import ", "keyword").map(x -> importDeclaration);
        Projection<JavaImportDeclaration> projection =
                Projector.get(Line.class).project(importDeclaration.name).map(JavaImportDeclaration::new);
        return label.combineHorizontal(projection);
    }
}
