package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import io.github.proton.plugins.java.tree.member.*;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import org.pf4j.Extension;

@Extension
public final class JavaMemberProjector implements Projector<JavaMember> {
    @Override
    public Class<JavaMember> clazz() {
        return JavaMember.class;
    }

    @Override
    public Projection<JavaMember> project(JavaMember member) {
        if (member instanceof JavaFieldMember fieldMember) {
            return Projector.get(JavaFieldMember.class).project(fieldMember).map(x -> x)
                .mapChars(c -> methodChar(c, fieldMember))
                .mapChars(c -> setFieldChar(c, fieldMember));
        } else if (member instanceof JavaSetFieldMember setFieldMember) {
            return Projector.get(JavaSetFieldMember.class).project(setFieldMember).map(x -> x)
                .mapChars(c -> methodChar(c, setFieldMember.fieldMember()));
        } else if (member instanceof JavaMethodMember methodMember) {
            return Projector.get(JavaMethodMember.class).project(methodMember).map(x -> x)
                .mapChars(c -> setFieldChar(c, new JavaFieldMember(methodMember.type(), methodMember.name())));
        } else {
            throw new RuntimeException();
        }
    }

    private <T extends JavaMember> Char<JavaMember> methodChar(Char<T> c, JavaFieldMember fieldMember) {
        return new Char<JavaMember>() {
            @Override
            public boolean decorative() {
                return c.decorative();
            }

            @Override
            public boolean mergeable() {
                return c.mergeable();
            }

            @Override
            public StyledCharacter character(Style style) {
                return c.character(style);
            }

            @Override
            public Option<JavaMember> insert(char character) {
                if (character == '(') {
                    return Option.some(c.insert(character).map(x -> (JavaMember) x)
                        .getOrElse(new JavaMethodMember(
                            fieldMember.type(),
                            fieldMember.name(),
                            Vector.empty())));
                } else {
                    return c.insert(character).map(x -> x);
                }
            }

            @Override
            public Option<JavaMember> delete() {
                return c.delete().map(x -> x);
            }
        };
    }


    private <T extends JavaMember> Char<JavaMember> setFieldChar(Char<T> c, JavaFieldMember fieldMember) {
        return new Char<JavaMember>() {
            @Override
            public boolean decorative() {
                return c.decorative();
            }

            @Override
            public boolean mergeable() {
                return c.mergeable();
            }

            @Override
            public StyledCharacter character(Style style) {
                return c.character(style);
            }

            @Override
            public Option<JavaMember> insert(char character) {
                if (character == '=') {
                    return Option.some(c.insert(character).map(x -> (JavaMember) x)
                        .getOrElse(new JavaSetFieldMember(
                            new JavaFieldMember(fieldMember.type(), fieldMember.name()),
                            new JavaExpression.Identifier(""))));
                } else {
                    return c.insert(character).map(x -> x);
                }
            }

            @Override
            public Option<JavaMember> delete() {
                return c.delete().map(x -> x);
            }
        };
    }
}
