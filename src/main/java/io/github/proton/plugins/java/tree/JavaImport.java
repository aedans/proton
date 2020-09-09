package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.ImportDeclaration;
import io.github.proton.editor.*;

public record JavaImport(JavaName name, boolean isStatic) implements Tree<JavaImport> {
    public static JavaImport from(ImportDeclaration tree) {
        var name = JavaName.from(tree.getName());
        var n = tree.isAsterisk() ? new JavaName(name.names().append(new JavaSimpleName("*"))) : name;
        return new JavaImport(n, tree.isStatic());
    }

    @Override
    public boolean isEmpty() {
        return name.isEmpty();
    }

    @Override
    public Projection<JavaImport> project() {
        var importLabel = TextProjection.label("import", "keyword").of(this);
        var staticLabel = TextProjection.label("static", "keyword").of(this);
        var nameProjection = name.project().map(n -> new JavaImport(n, isStatic));
        return importLabel
            .combine(TextProjection.space.of(this))
            .combine(isStatic ? staticLabel.combine(TextProjection.space.of(this)) : Projection.empty())
            .combine(nameProjection)
            .combine(TextProjection.semicolon.of(this));
    }
}
