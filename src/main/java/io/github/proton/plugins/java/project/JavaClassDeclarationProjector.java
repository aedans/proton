/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.project;

import io.github.proton.editor.Projection;
import io.github.proton.editor.Projector;
import io.github.proton.editor.VectorProjection;
import io.github.proton.plugins.java.tree.JavaClassDeclaration;
import io.github.proton.plugins.java.tree.JavaFieldMember;
import io.github.proton.plugins.java.tree.JavaIdentifier;
import io.github.proton.plugins.java.tree.JavaType;
import org.pf4j.Extension;

@Extension
public final class JavaClassDeclarationProjector implements Projector<JavaClassDeclaration> {
    @Override
    public Class<JavaClassDeclaration> clazz() {
        return JavaClassDeclaration.class;
    }

    @Override
    public Projection<JavaClassDeclaration> project(JavaClassDeclaration classDeclaration) {
        Projection<JavaClassDeclaration> label = Projection.label("class ", "keyword").of(classDeclaration);
        Projection<JavaClassDeclaration> projection = Projector.get(JavaIdentifier.class)
                .project(classDeclaration.name)
                .map(name -> new JavaClassDeclaration(name, classDeclaration.fields));
        Projection<JavaClassDeclaration> openBracket = Projection.openBracket.of(classDeclaration);
        Projection<JavaClassDeclaration> closeBracket = Projection.closeBracket.of(classDeclaration);
        Projection<JavaClassDeclaration> fields = new VectorProjection<>(
                        classDeclaration.fields,
                        Projector.get(JavaFieldMember.class),
                        Projection.newline(),
                        new JavaFieldMember(
                                new JavaType.ClassOrInterface(new JavaIdentifier("")), new JavaIdentifier("")))
                .map(x -> new JavaClassDeclaration(classDeclaration.name, x));
        return label.combine(projection)
                .combine(openBracket.combine(Projection.newline()).combine(fields).indent(2))
                .combine(Projection.newline())
                .combine(closeBracket)
                .combine(Projection.newline());
    }
}
