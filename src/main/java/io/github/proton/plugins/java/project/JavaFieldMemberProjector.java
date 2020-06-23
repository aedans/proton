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
            .map(t -> new JavaFieldMember(t, fieldMember.name(), fieldMember.expression()));
        var name = Projector.get(JavaIdentifier.class)
            .project(fieldMember.name())
            .map(n -> new JavaFieldMember(fieldMember.type(), n, fieldMember.expression()));
        var expr = new OptionProjection<>(
            fieldMember.expression(),
            new Projector<JavaExpression>() {
                Projector<JavaExpression> projector = Projector.get(JavaExpression.class);

                @Override
                public Class<JavaExpression> clazz() {
                    return JavaExpression.class;
                }

                @Override
                public Projection<JavaExpression> project(JavaExpression expression) {
                    var eq = Projection.label("=", "punctuation.eq").of(expression);
                    return eq.combine(projector.project(expression));
                }
            },
            new JavaExpression.Identifier("")
        ).map(e -> new JavaFieldMember(fieldMember.type(), fieldMember.name(), e));
        return type.combine(name).combine(expr);
    }
}
