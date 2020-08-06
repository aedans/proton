package io.github.proton.plugins.java.tree;

import com.sun.source.tree.PackageTree;

public record JavaPackage(JavaQualifiedIdentifier identifier) {
    public static JavaPackage from(PackageTree tree) {
        return new JavaPackage(JavaQualifiedIdentifier.from(tree.getPackageName()));
    }

    public boolean isEmpty() {
        return identifier.isEmpty();
    }
}
