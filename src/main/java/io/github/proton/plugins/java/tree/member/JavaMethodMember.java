package io.github.proton.plugins.java.tree.member;

import io.github.proton.plugins.java.tree.*;
import io.vavr.collection.Vector;

public record JavaMethodMember(JavaType type,
                               JavaIdentifier name,
                               Vector<JavaStatement>statements) implements JavaMember {
    @Override
    public boolean isEmpty() {
        return type.isEmpty() && name.isEmpty() && statements.isEmpty();
    }
}
