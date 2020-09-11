package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.body.*;
import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.declaration.*;
import io.github.proton.plugins.java.tree.type.JavaClassOrInterfaceType;
import io.vavr.collection.Vector;

public interface JavaBodyDeclaration extends Tree<JavaBodyDeclaration> {
    static JavaBodyDeclaration from(BodyDeclaration<?> declaration) {
        if (declaration instanceof FieldDeclaration field) {
            return JavaFieldDeclaration.from(field);
        } else if (declaration instanceof MethodDeclaration method) {
            return JavaMethodDeclaration.from(method);
        } else {
            return new JavaBodyDeclaration() {
                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public Projection<JavaBodyDeclaration> project() {
                    return TextProjection.text("invalid declaration", "invalid.illegal").of(this);
                }
            };
        }
    }
}
