/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.project;

import io.github.proton.display.GroupProjection;
import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.github.proton.plugins.java.tree.JavaClassDeclaration;
import io.github.proton.plugins.java.tree.JavaFieldMember;
import io.github.proton.plugins.text.LabelProjection;
import io.github.proton.plugins.text.Line;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import org.pf4j.Extension;

@Extension
public final class JavaClassDeclarationProjector implements Projector<JavaClassDeclaration> {
    @Override
    public Class<JavaClassDeclaration> clazz() {
        return JavaClassDeclaration.class;
    }

    @Override
    public Projection<JavaClassDeclaration> project(JavaClassDeclaration classDeclaration) {
        Projection<JavaClassDeclaration> label = new LabelProjection("class ", "keyword").map(x -> classDeclaration);
        Projection<JavaClassDeclaration> projection = Projector.get(Line.class)
                .project(classDeclaration.name)
                .map(name -> new JavaClassDeclaration(name, classDeclaration.fields));
        Projection<JavaClassDeclaration> openBracket = LabelProjection.openBracket.map(x -> classDeclaration);
        Projection<JavaClassDeclaration> closeBracket = LabelProjection.closeBracket.map(x -> classDeclaration);
        Projection<JavaClassDeclaration> fields = new GroupProjection<JavaClassDeclaration, JavaFieldMember>() {
            @Override
            public Projection<JavaFieldMember> projectElem(JavaFieldMember elem) {
                return Projector.get(JavaFieldMember.class).project(elem);
            }

            @Override
            public Vector<JavaFieldMember> getElems() {
                return classDeclaration.fields;
            }

            @Override
            public JavaClassDeclaration setElems(Vector<JavaFieldMember> elems) {
                return new JavaClassDeclaration(classDeclaration.name, elems);
            }

            @Override
            public Option<JavaFieldMember> newElem() {
                return Option.some(new JavaFieldMember(new Line("")));
            }
        };
        return label.combineHorizontal(projection)
                .combineHorizontal(openBracket)
                .combineVertical(fields.indent(2))
                .combineVertical(closeBracket);
    }
}
