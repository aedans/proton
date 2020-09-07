package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.body.TypeDeclaration;
import io.github.proton.editor.*;

public record JavaClass(JavaSimpleName name) implements Tree<JavaClass> {
    public static JavaClass from(TypeDeclaration<?> tree) {
        return new JavaClass(JavaSimpleName.from(tree.getName()));
    }

    public boolean isEmpty() {
        return name.isEmpty();
    }

    @Override
    public Projection<JavaClass> project() {
        var label = TextProjection.label("class", "keyword").of(this);
        var n = name.project().map(JavaClass::new);
        return label
            .combine(TextProjection.space.of(this))
            .combine(n)
            .combine(TextProjection.space.of(this))
            .combine(TextProjection.openBracket.of(this))
            .combine(TextProjection.closeBracket.of(this));
    }
}
