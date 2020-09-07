package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.PackageDeclaration;
import io.github.proton.editor.*;

public record JavaPackage(JavaName name) implements Tree<JavaPackage> {
    public static JavaPackage from(PackageDeclaration tree) {
        return new JavaPackage(JavaName.from(tree.getName()));
    }

    public boolean isEmpty() {
        return name.isEmpty();
    }


    @Override
    public Projection<JavaPackage> project() {
        var label = TextProjection.label("package", "keyword").of(this);
        var projection = name.project().map(JavaPackage::new);
        return label
            .combine(TextProjection.space.of(this))
            .combine(projection);
    }
}
