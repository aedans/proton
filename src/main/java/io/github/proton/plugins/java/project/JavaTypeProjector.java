package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import org.pf4j.Extension;

@Extension
public final class JavaTypeProjector implements Projector<JavaType> {
    @Override
    public Class<JavaType> clazz() {
        return JavaType.class;
    }

    @Override
    public Projection<JavaType> project(JavaType javaType) {
        if (javaType instanceof JavaType.Primitive primitive) {
            return Projector.get(JavaIdentifier.class)
                .project(new JavaIdentifier(primitive.name().toLowerCase()))
                .map(JavaType::fromIdentifier)
                .mapChar(c -> c.withStyle("keyword"));
        } else if (javaType instanceof JavaType.ClassOrInterface classOrInterface) {
            return Projector.get(JavaIdentifier.class)
                .project(classOrInterface.identifier())
                .map(JavaType::fromIdentifier);
        } else {
            throw new RuntimeException();
        }
    }
}
