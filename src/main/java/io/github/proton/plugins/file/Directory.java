/*
 * Copyright 2020 Aedan Smith
 */
package io.github.proton.plugins.file;

import io.vavr.collection.Vector;

public final class Directory {
    public final Vector<FileLink> files;

    public Directory(Vector<FileLink> files) {
        this.files = files;
    }
}
