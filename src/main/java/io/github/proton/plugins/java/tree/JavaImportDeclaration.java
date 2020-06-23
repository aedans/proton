package io.github.proton.plugins.java.tree;

public record JavaImportDeclaration(JavaIdentifier name) {
    public boolean isEmpty() {
        return name.isEmpty();
    }
}
