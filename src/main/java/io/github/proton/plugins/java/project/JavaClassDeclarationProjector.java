package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import io.vavr.control.Option;
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
        var name = Projector.get(JavaIdentifier.class)
            .project(classDeclaration.name())
            .map(n -> new JavaClassDeclaration(n, classDeclaration.fields()));
        var fields = new VectorProjection<>(
            classDeclaration.fields(),
            Projector.get(JavaFieldMember.class),
            Projection.newline(),
            new JavaFieldMember(
                new JavaType.ClassOrInterface(new JavaIdentifier("")),
                new JavaIdentifier(""),
                Option.none())
        ).map(fs -> new JavaClassDeclaration(classDeclaration.name(), fs));
        return label
            .combine(Projection.space.of(classDeclaration))
            .combine(name)
            .combine(Projection.space.of(classDeclaration))
            .combine(Projection.openBracket.of(classDeclaration)
                .combine(Projection.space.of(classDeclaration))
                .combine(Projection.newline())
                .combine(fields)
                .indent(2))
            .combine(Projection.newline())
            .combine(Projection.closeBracket.of(classDeclaration))
            .combine(Projection.newline());
    }
}
