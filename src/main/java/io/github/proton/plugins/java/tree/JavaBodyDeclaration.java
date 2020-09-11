package io.github.proton.plugins.java.tree;

import com.github.javaparser.ast.body.*;
import io.github.proton.editor.Tree;
import io.github.proton.plugins.java.tree.declaration.*;

public interface JavaBodyDeclaration extends Tree<JavaBodyDeclaration> {
    static JavaBodyDeclaration from(BodyDeclaration<?> declaration) {
        if (declaration instanceof FieldDeclaration field) {
            return JavaFieldDeclaration.from(field);
        } else if (declaration instanceof MethodDeclaration method) {
            return JavaMethodDeclaration.from(method);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
