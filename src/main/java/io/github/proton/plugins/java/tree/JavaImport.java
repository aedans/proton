package io.github.proton.plugins.java.tree;

import com.sun.source.tree.ImportTree;

public record JavaImport(JavaQualifiedIdentifier identifier, boolean isStatic) {
    public static JavaImport from(ImportTree tree) {
        return new JavaImport(JavaQualifiedIdentifier.from(tree.getQualifiedIdentifier()), tree.isStatic());
    }

    public boolean isEmpty() {
        return identifier.isEmpty();
    }
}
