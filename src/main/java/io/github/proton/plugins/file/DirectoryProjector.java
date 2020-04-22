/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.file;

import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.github.proton.display.VectorProjection;
import org.pf4j.Extension;

@Extension
public final class DirectoryProjector implements Projector<Directory> {
    @Override
    public Class<Directory> clazz() {
        return Directory.class;
    }

    @Override
    public Projection<Directory> project(Directory directory) {
        return new VectorProjection<>(directory.files, new FileLinkProjector()).map(Directory::new);
    }
}
