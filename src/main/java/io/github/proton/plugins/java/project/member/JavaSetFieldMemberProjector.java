package io.github.proton.plugins.java.project.member;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.JavaExpression;
import io.github.proton.plugins.java.tree.member.*;
import org.pf4j.Extension;

@Extension
public final class JavaSetFieldMemberProjector implements Projector<JavaSetFieldMember> {
    @Override
    public Class<JavaSetFieldMember> clazz() {
        return JavaSetFieldMember.class;
    }

    @Override
    public Projection<JavaSetFieldMember> project(JavaSetFieldMember setFieldMember) {
        var fieldMember = Projector.get(JavaFieldMember.class)
            .project(setFieldMember.fieldMember())
            .map(f -> new JavaSetFieldMember(f, setFieldMember.expression()));
        var expression = Projector.get(JavaExpression.class)
            .project(setFieldMember.expression())
            .map(e -> new JavaSetFieldMember(setFieldMember.fieldMember(), e));
        return fieldMember
            .combine(TextProjection.space.of(setFieldMember))
            .combine(TextProjection.label("=", "punctuation.eq").of(setFieldMember))
            .combine(TextProjection.space.of(setFieldMember))
            .combine(expression);
    }
}
