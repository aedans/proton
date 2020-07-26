package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import io.github.proton.plugins.java.tree.member.JavaFieldMember;
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
        Projector<JavaMember> memberProjector = Projector.get(JavaMember.class);
        var members = new VectorProjection<>(
            classDeclaration.members(),
            memberProjector::project,
            new JavaFieldMember(
                new JavaType.ClassOrInterface(new JavaIdentifier("")),
                new JavaIdentifier("")),
            Projection.newline(),
            JavaMember::isEmpty,
            x -> x == '\n'
        ).map(ms -> new JavaClassDeclaration(classDeclaration.name(), ms));
        return label
            .combine(TextProjection.space.of(classDeclaration))
            .combine(name)
            .combine(Projection.<JavaClassDeclaration>newline().combine(members).indent(2));
    }
}
