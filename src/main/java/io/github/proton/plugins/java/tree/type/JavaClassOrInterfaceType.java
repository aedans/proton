package io.github.proton.plugins.java.tree.type;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import io.github.proton.editor.Projection;
import io.github.proton.plugins.java.tree.*;

public record JavaClassOrInterfaceType(JavaSimpleName name) implements JavaType {
    public static JavaClassOrInterfaceType from(ClassOrInterfaceType type) {
        return new JavaClassOrInterfaceType(JavaSimpleName.from(type.getName()));
    }

    @Override
    public boolean isEmpty() {
        return name.isEmpty();
    }

    @Override
    public Projection<JavaType> project() {
        return name.project().map(JavaType::from);
    }
}
