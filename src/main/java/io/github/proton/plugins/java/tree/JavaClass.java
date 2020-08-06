package io.github.proton.plugins.java.tree;

import com.sun.source.tree.*;
import com.sun.source.util.SimpleTreeVisitor;

public record JavaClass(JavaName name) {
    public boolean isEmpty() {
        return name.isEmpty();
    }

    public static JavaClass from(Tree tree) {
        return new SimpleTreeVisitor<JavaClass, Object>(null) {
            @Override
            public JavaClass visitClass(ClassTree node, Object o) {
                return from(node);
            }
        }.visit(tree, null);
    }

    public static JavaClass from(ClassTree tree) {
        return new JavaClass(new JavaName(tree.getSimpleName().toString()));
    }
}
