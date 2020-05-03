/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.project;

import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.github.proton.plugins.java.tree.JavaFieldMember;
import io.github.proton.plugins.java.tree.JavaIdentifier;
import io.github.proton.plugins.java.tree.JavaType;
import org.pf4j.Extension;

@Extension
public final class JavaFieldMemberProjector implements Projector<JavaFieldMember> {
    @Override
    public Class<JavaFieldMember> clazz() {
        return JavaFieldMember.class;
    }

    @Override
    public Projection<JavaFieldMember> project(JavaFieldMember fieldMember) {
        Projection<JavaFieldMember> type = Projector.get(JavaType.class)
                .project(fieldMember.type)
                .map(t -> new JavaFieldMember(t, fieldMember.name));
        Projection<JavaFieldMember> name = Projector.get(JavaIdentifier.class)
                .project(fieldMember.name)
                .map(n -> new JavaFieldMember(fieldMember.type, n));
        return type.combineHorizontal(name);
    }
}
