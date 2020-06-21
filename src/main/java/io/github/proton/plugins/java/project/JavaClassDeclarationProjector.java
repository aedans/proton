package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import org.pf4j.Extension;

@Extension
public final class JavaClassDeclarationProjector implements Projector<JavaClassDeclaration> {
    @Override
    public Class<JavaClassDeclaration> clazz() {
        return JavaClassDeclaration.class;
    }

    @Override
    public Projection<JavaClassDeclaration> project(JavaClassDeclaration classDeclaration) {
        var label = Projection.label("class", "keyword").of(classDeclaration);
        var projection = Projector.get(JavaIdentifier.class)
                .project(classDeclaration.name())
                .map(name -> new JavaClassDeclaration(name, classDeclaration.fields()));
        var openBracket = Projection.openBracket.of(classDeclaration);
        var closeBracket = Projection.closeBracket.of(classDeclaration);
        var fields = new VectorProjection<>(
                classDeclaration.fields(),
                Projector.get(JavaFieldMember.class),
                Projection.newline(),
                new JavaFieldMember(
                        new JavaType.ClassOrInterface(new JavaIdentifier("")), new JavaIdentifier("")))
                .map(x -> new JavaClassDeclaration(classDeclaration.name(), x));
        return label.combine(projection)
                .combine(openBracket
                        .combine(Projection.newline())
                        .combine(fields)
                        .indent(2))
                .combine(Projection.newline())
                .combine(closeBracket)
                .combine(Projection.newline());
    }
}
