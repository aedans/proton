/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.file;

import io.github.proton.display.GroupProjection;
import io.github.proton.display.Projection;
import io.github.proton.display.Projector;
import io.vavr.collection.Vector;
import org.pf4j.Extension;

@Extension
public final class DirectoryProjector implements Projector<Directory> {
    @Override
    public Class<Directory> clazz() {
        return Directory.class;
    }

    @Override
    public Projection<Directory> project(Directory directory) {
        return new GroupProjection<Directory, FileLink>() {
            @Override
            public Projection<FileLink> projectElem(FileLink file) {
                return new FileLinkProjector().project(file);
            }

            @Override
            public Vector<FileLink> getElems() {
                return directory.files;
            }

            @Override
            public Directory setElems(Vector<FileLink> files) {
                return new Directory(files);
            }
        };
    }
}
