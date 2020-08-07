package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.PackageDeclaration;

public record JavaPackage(JavaName name) {
    public static JavaPackage from(PackageDeclaration tree) {
        return new JavaPackage(JavaName.from(tree.getName()));
    }

    public boolean isEmpty() {
        return name.isEmpty();
    }
}
