package io.github.proton.plugins.java.project;

import io.github.proton.editor.*;
import io.github.proton.plugins.java.tree.*;
import io.vavr.control.Option;
import org.pf4j.Extension;

@Extension
public final class JavaImportProjector implements Projector<JavaImport> {
    @Override
    public Class<JavaImport> clazz() {
        return JavaImport.class;
    }

    @Override
    public Projection<JavaImport> project(JavaImport javaImport) {
        var importLabel = TextProjection.label("import", "keyword").of(javaImport);
        var staticLabel = TextProjection.label("static", "keyword").of(javaImport);
        var nameProjection = Projector.get(JavaQualifiedIdentifier.class)
            .project(javaImport.identifier())
            .map(n -> new JavaImport(n, javaImport.isStatic()));
        return importLabel
            .combine(TextProjection.space.of(javaImport))
            .combine(javaImport.isStatic() ? staticLabel.combine(TextProjection.space.of(javaImport)) : Projection.empty())
            .combine(nameProjection);
    }
}
