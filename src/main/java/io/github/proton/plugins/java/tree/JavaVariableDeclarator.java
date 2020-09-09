package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.body.VariableDeclarator;
import io.github.proton.editor.*;

public record JavaVariableDeclarator(JavaSimpleName name) implements Tree<JavaVariableDeclarator> {
    public static JavaVariableDeclarator from(VariableDeclarator variable) {
        return new JavaVariableDeclarator(JavaSimpleName.from(variable.getName()));
    }

    @Override
    public boolean isEmpty() {
        return name.isEmpty();
    }

    @Override
    public Projection<JavaVariableDeclarator> project() {
        return name.project().map(JavaVariableDeclarator::new);
    }
}
