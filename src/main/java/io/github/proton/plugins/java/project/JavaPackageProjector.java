package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import org.pf4j.Extension;

@Extension
public final class JavaPackageProjector implements Projector<JavaPackage> {
    @Override
    public Class<JavaPackage> clazz() {
        return JavaPackage.class;
    }

    @Override
    public Projection<JavaPackage> project(JavaPackage javaPackage) {
        var label = TextProjection.label("package", "keyword").of(javaPackage);
        var projection = Projector.get(JavaName.class)
            .project(javaPackage.name())
            .map(JavaPackage::new);
        return label
            .combine(TextProjection.space.of(javaPackage))
            .combine(projection);
    }
}
