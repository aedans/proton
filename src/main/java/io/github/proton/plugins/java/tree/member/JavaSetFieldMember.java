package io.github.proton.plugins.java.tree.member;

import io.github.proton.plugins.java.tree.*;

public record JavaSetFieldMember(JavaFieldMember fieldMember,
                                 JavaExpression expression) implements JavaMember {
    @Override
    public boolean isEmpty() {
        return fieldMember.isEmpty() && expression.isEmpty();
    }
}
