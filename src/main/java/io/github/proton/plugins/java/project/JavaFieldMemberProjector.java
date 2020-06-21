package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import org.pf4j.Extension;

@Extension
public final class JavaFieldMemberProjector implements Projector<JavaFieldMember> {
    @Override
    public Class<JavaFieldMember> clazz() {
        return JavaFieldMember.class;
    }

    @Override
    public Projection<JavaFieldMember> project(JavaFieldMember fieldMember) {
        var type = Projector.get(JavaType.class)
                .project(fieldMember.type())
                .map(t -> new JavaFieldMember(t, fieldMember.name()));
        var name = Projector.get(JavaIdentifier.class)
                .project(fieldMember.name())
                .map(n -> new JavaFieldMember(fieldMember.type(), n));
        return type.combine(name);
    }
}
