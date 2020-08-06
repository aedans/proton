package io.github.proton.plugins.java.tree;

import com.sun.source.tree.*;
import com.sun.source.util.SimpleTreeVisitor;
import io.vavr.collection.Vector;

public record JavaQualifiedIdentifier(Vector<JavaName> names) {
    public static JavaQualifiedIdentifier from(Tree tree) {
        return new SimpleTreeVisitor<JavaQualifiedIdentifier, Object>() {
            @Override
            public JavaQualifiedIdentifier visitMemberSelect(MemberSelectTree node, Object o) {
                return new JavaQualifiedIdentifier(from(node.getExpression()).names.append(new JavaName(node.getIdentifier())));
            }

            @Override
            public JavaQualifiedIdentifier visitIdentifier(IdentifierTree node, Object o) {
                return new JavaQualifiedIdentifier(Vector.of(new JavaName(node.getName())));
            }
        }.visit(tree, null);
    }

    public boolean isEmpty() {
        return names.isEmpty() || names.get().isEmpty();
    }

    @Override
    public String toString() {
        return names.mkString(".");
    }
}
