package io.github.proton.plugins.java.tree.declaration;

import com.github.javaparser.ast.body.MethodDeclaration;
import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import io.github.proton.plugins.java.tree.statement.JavaNameStatement;
import io.github.proton.plugins.java.tree.type.JavaClassOrInterfaceType;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

public record JavaMethodDeclaration(JavaType type,
                                    JavaSimpleName name,
                                    Vector<JavaParameter> parameters,
                                    Option<Vector<JavaStatement>> body) implements JavaBodyDeclaration {
    public static JavaMethodDeclaration from(MethodDeclaration method) {
        return new JavaMethodDeclaration(
            JavaType.from(method.getType()),
            JavaSimpleName.from(method.getName()),
            Vector.ofAll(method.getParameters()).map(JavaParameter::from),
            Option.ofOptional(method.getBody()).map(x -> Vector.ofAll(x.getStatements()).map(JavaStatement::from)));
    }

    @Override
    public boolean isEmpty() {
        return type.isEmpty() && name.isEmpty() && parameters.isEmpty();
    }

    @Override
    public Projection<JavaBodyDeclaration> project() {
        Projection<JavaBodyDeclaration> typeProjection = type.project()
            .map(type -> new JavaMethodDeclaration(type, name, parameters, body));
        Projection<JavaBodyDeclaration> nameProjection = name.project()
            .map(name -> new JavaMethodDeclaration(type, name, parameters, body));
        Projection<JavaBodyDeclaration> parametersProjection = new VectorProjection<>(
            parameters,
            new JavaParameter(new JavaClassOrInterfaceType(new JavaSimpleName("")), new JavaSimpleName("")),
            TextProjection.comma.combine(TextProjection.space),
            x -> x == ','
        ).map(parameters -> new JavaMethodDeclaration(type, name, parameters, body));
        if (parameters.isEmpty()) {
            nameProjection = nameProjection.mapChar(c -> c.withDelete(() ->
                Option.some(c.delete().getOrElse(new JavaFieldDeclaration(type, Vector.of(new JavaVariableDeclarator(name)))))));
        }
        var signatureProjection = typeProjection
            .combine(TextProjection.space.of(this))
            .combine(nameProjection)
            .combine(TextProjection.openParen.of(this))
            .combine(parametersProjection)
            .combine(TextProjection.closeParen.of(this));
        return body.map(statements -> {
            Projection<JavaBodyDeclaration> bodyProjection = new VectorProjection<>(
                statements,
                new JavaNameStatement(new JavaSimpleName("")),
                Projection.newline(),
                c -> c == '\n'
            ).map(body -> new JavaMethodDeclaration(type, name, parameters, Option.some(body)));
            Projection<JavaBodyDeclaration> space = TextProjection.text(" ", "").of((JavaBodyDeclaration) this)
                .mapChar(c -> c.withDelete(() -> Option.some(new JavaMethodDeclaration(type, name, parameters, Option.none()))));
            return signatureProjection
                .combine(space)
                .combine(TextProjection.openBracket.of(this))
                .combine(Projection.<JavaBodyDeclaration>newline()
                    .combine(bodyProjection)
                    .indent(2)
                    .group())
                .combine(Projection.trailingNewline())
                .combine(TextProjection.closeBracket.of(this));
        }).getOrElse(() -> signatureProjection.combine(TextProjection.semicolon.of(this))
            .mapChar(c -> c.withInsert(character -> character == '{'
                ? Option.some(new JavaMethodDeclaration(type, name, parameters, Option.some(Vector.empty())))
                : c.insert(character))));
    }
}
