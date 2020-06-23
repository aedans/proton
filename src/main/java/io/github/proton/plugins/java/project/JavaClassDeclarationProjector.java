package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import io.github.proton.plugins.java.tree.member.*;
import io.vavr.collection.Vector;
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
        var label = TextProjection.label("class", "keyword").of(classDeclaration);
        var name = Projector.get(JavaIdentifier.class)
            .project(classDeclaration.name())
            .map(n -> new JavaClassDeclaration(n, classDeclaration.members()));
        var members = new VectorProjection<>(
            classDeclaration.members(),
            Projector.get(JavaMember.class),
            Projection.newline(),
            new JavaFieldMember(
                new JavaType.ClassOrInterface(new JavaIdentifier("")),
                new JavaIdentifier("")),
            JavaMember::isEmpty
        ).map(ms -> new JavaClassDeclaration(classDeclaration.name(), ms));
        return label
            .combine(TextProjection.space.of(classDeclaration))
            .combine(name)
            .combine(TextProjection.space.of(classDeclaration))
            .combine(Projection.newline().of(classDeclaration)
                .combine(members)
                .indent(2));
    }
}
