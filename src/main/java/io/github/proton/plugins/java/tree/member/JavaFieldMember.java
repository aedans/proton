package io.github.proton.plugins.java.tree.member;

import io.github.proton.plugins.java.tree.*;

public record JavaFieldMember(JavaType type, JavaIdentifier name) implements JavaMember {
    @Override
    public boolean isEmpty() {
        return type.isEmpty() && name.isEmpty();
    }
}
