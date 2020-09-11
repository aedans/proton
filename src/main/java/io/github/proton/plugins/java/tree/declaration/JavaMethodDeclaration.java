package io.github.proton.plugins.java.tree.declaration;

import com.github.javaparser.ast.body.*;
import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import io.github.proton.plugins.java.tree.type.JavaClassOrInterfaceType;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

public record JavaMethodDeclaration(JavaType type,
                                    JavaSimpleName name,
                                    Vector<JavaParameter> parameters) implements JavaBodyDeclaration {
    public static JavaMethodDeclaration from(MethodDeclaration method) {
        return new JavaMethodDeclaration(
            JavaType.from(method.getType()),
            JavaSimpleName.from(method.getName()),
            Vector.ofAll(method.getParameters()).map(JavaParameter::from));
    }

    @Override
    public boolean isEmpty() {
        return type.isEmpty() && name.isEmpty() && parameters.isEmpty();
    }

    @Override
    public Projection<JavaBodyDeclaration> project() {
        Projection<JavaBodyDeclaration> typeProjection = type.project()
            .map(type -> new JavaMethodDeclaration(type, name, parameters));
        Projection<JavaBodyDeclaration> nameProjection = name.project()
            .map(name -> new JavaMethodDeclaration(type, name, parameters));
        Projection<JavaBodyDeclaration> parametersProjection = new VectorProjection<>(
            parameters,
            new JavaParameter(new JavaClassOrInterfaceType(new JavaSimpleName("")), new JavaSimpleName("")),
            TextProjection.comma.combine(TextProjection.space),
            x -> x == ','
        ).map(parameters -> new JavaMethodDeclaration(type, name, parameters));
        if (parameters.isEmpty()) {
            nameProjection = nameProjection.mapChar(c -> c.withDelete(() ->
                Option.some(c.delete().getOrElse(new JavaFieldDeclaration(type, Vector.of(new JavaVariableDeclarator(name)))))));
        }
        return typeProjection
            .combine(TextProjection.space.of(this))
            .combine(nameProjection)
            .combine(TextProjection.openParen.of(this))
            .combine(parametersProjection)
            .combine(TextProjection.closeParen.of(this))
            .combine(TextProjection.semicolon.of(this));
    }
}
