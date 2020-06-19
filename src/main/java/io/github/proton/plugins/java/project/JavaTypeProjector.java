/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.java.project;

import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.github.proton.display.Style;
import io.github.proton.display.StyledCharacter;
import io.github.proton.plugins.java.tree.JavaIdentifier;
import io.github.proton.plugins.java.tree.JavaType;
import io.vavr.control.Option;
import org.pf4j.Extension;

@Extension
public final class JavaTypeProjector implements Projector<JavaType> {
    @Override
    public Class<JavaType> clazz() {
        return JavaType.class;
    }

    @Override
    public Projection<JavaType> project(JavaType javaType) {
        return javaType.match(
                primitive -> Projector.get(JavaIdentifier.class)
                        .project(new JavaIdentifier(primitive.name().toLowerCase()))
                        .map(JavaType::fromIdentifier)
                        .mapChars(c -> new Projection.Char<JavaType>() {
                            @Override
                            public boolean decorative() {
                                return c.decorative();
                            }

                            @Override
                            public StyledCharacter character(Style style) {
                                return c.character(style.of("keyword"));
                            }

                            @Override
                            public Option<JavaType> insert(char character) {
                                return c.insert(character);
                            }

                            @Override
                            public Option<JavaType> delete() {
                                return c.delete();
                            }

                            @Override
                            public Option<JavaType> submit() {
                                return c.submit();
                            }
                        }),
                classOrInterface -> Projector.get(JavaIdentifier.class)
                        .project(classOrInterface.name)
                        .map(JavaType::fromIdentifier));
    }
}
