package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.body.TypeDeclaration;

public record JavaClass(JavaSimpleName name) {
    public boolean isEmpty() {
        return name.isEmpty();
    }

    public static JavaClass from(TypeDeclaration<?> tree) {
        return new JavaClass(JavaSimpleName.from(tree.getName()));
    }
}
