package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.body.TypeDeclaration;
import io.github.proton.editor.*;
import io.vavr.collection.Vector;

public record JavaClass(JavaSimpleName name,
                        Vector<JavaBodyDeclaration> declarations) implements Tree<JavaClass> {
    public static JavaClass from(TypeDeclaration<?> tree) {
        return new JavaClass(
            JavaSimpleName.from(tree.getName()),
            Vector.ofAll(tree.getMembers()).map(JavaBodyDeclaration::from));
    }

    @Override
    public boolean isEmpty() {
        return name.isEmpty();
    }

    @Override
    public Projection<JavaClass> project() {
        var label = TextProjection.label("class", "keyword").of(this);
        var n = name.project().map(name -> new JavaClass(name, declarations));
        var decls = new VectorProjection<>(
            declarations,
            new JavaFieldDeclaration(new JavaClassOrInterfaceType(new JavaSimpleName("")), Vector.empty()),
            Projection.newline(),
            c -> c == '\n'
        ).map(declarations -> new JavaClass(name, declarations));
        return label
            .combine(TextProjection.space.of(this))
            .combine(n)
            .combine(TextProjection.space.of(this))
            .combine(TextProjection.openBracket.of(this))
            .combine(Projection.<JavaClass>newline()
                .combine(decls)
                .indent(2)
                .group()
                .combine(Projection.newline()))
            .combine(TextProjection.closeBracket.of(this));
    }
}
