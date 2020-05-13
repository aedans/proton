/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.project;

import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.github.proton.display.VectorProjection;
import io.github.proton.plugins.java.tree.JavaClassDeclaration;
import io.github.proton.plugins.java.tree.JavaFieldMember;
import io.github.proton.plugins.java.tree.JavaIdentifier;
import io.github.proton.plugins.java.tree.JavaType;
import io.github.proton.plugins.text.LabelProjection;
import org.pf4j.Extension;

@Extension
public final class JavaClassDeclarationProjector implements Projector<JavaClassDeclaration> {
    @Override
    public Class<JavaClassDeclaration> clazz() {
        return JavaClassDeclaration.class;
    }

    @Override
    public Projection<JavaClassDeclaration> project(JavaClassDeclaration classDeclaration) {
        Projection<JavaClassDeclaration> label = new LabelProjection("class ", "keyword").of(classDeclaration);
        Projection<JavaClassDeclaration> projection = Projector.get(JavaIdentifier.class)
                .project(classDeclaration.name)
                .map(name -> new JavaClassDeclaration(name, classDeclaration.fields));
        Projection<JavaClassDeclaration> openBracket = LabelProjection.openBracket.of(classDeclaration);
        Projection<JavaClassDeclaration> closeBracket = LabelProjection.closeBracket.of(classDeclaration);
        Projection<JavaClassDeclaration> fields = new VectorProjection<>(
                        classDeclaration.fields, Projector.get(JavaFieldMember.class), new JavaFieldMember(
                                new JavaType.ClassOrInterface(new JavaIdentifier("")), new JavaIdentifier("")))
                .map(x -> new JavaClassDeclaration(classDeclaration.name, x));
        return label.combineHorizontal(projection)
                .combineHorizontal(openBracket)
                .combineVertical(fields.indent(2))
                .combineVertical(closeBracket)
                .indentVertical(1);
    }
}
