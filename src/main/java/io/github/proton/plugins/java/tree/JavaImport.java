package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.ImportDeclaration;

public record JavaImport(JavaName name, boolean isStatic) {
    public static JavaImport from(ImportDeclaration tree) {
        var name = JavaName.from(tree.getName());
        var n = tree.isAsterisk() ? new JavaName(name.names().append(new JavaSimpleName("*"))) : name;
        return new JavaImport(n, tree.isStatic());
    }

    public boolean isEmpty() {
        return name.isEmpty();
    }
}
