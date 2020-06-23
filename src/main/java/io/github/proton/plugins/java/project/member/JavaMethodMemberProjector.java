package io.github.proton.plugins.java.project.member;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import io.github.proton.plugins.java.tree.member.JavaMethodMember;
import org.pf4j.Extension;

@Extension
public final class JavaMethodMemberProjector implements Projector<JavaMethodMember> {
    @Override
    public Class<JavaMethodMember> clazz() {
        return JavaMethodMember.class;
    }

    @Override
    public Projection<JavaMethodMember> project(JavaMethodMember methodMember) {
        var type = Projector.get(JavaType.class)
            .project(methodMember.type())
            .map(t -> new JavaMethodMember(t, methodMember.name(), methodMember.expressions()));
        var name = Projector.get(JavaIdentifier.class)
            .project(methodMember.name())
            .map(n -> new JavaMethodMember(methodMember.type(), n, methodMember.expressions()));
        var expressions = new VectorProjection<>(
            methodMember.expressions(),
            Projector.get(JavaExpression.class),
            Projection.newline(),
            new JavaExpression.Identifier(""),
            JavaExpression::isEmpty
        ).map(es -> new JavaMethodMember(methodMember.type(), methodMember.name(), es));
        return type
            .combine(TextProjection.space.of(methodMember))
            .combine(name)
            .combine(TextProjection.openParen.of(methodMember))
            .combine(TextProjection.closeParen.of(methodMember))
            .combine(TextProjection.space.of(methodMember))
            .combine(Projection.newline().of(methodMember))
                .combine(expressions)
                .indent(2);
    }
}