package io.github.proton.plugins.file;

import io.github.proton.file.FileReader;

import java.io.File;

public final class FileLink {
    public final Object tree;
    public final File file;
    public final boolean open;

    public FileLink(File file) {
        this(FileReader.instance.read(file).get(), file, false);
    }

    public FileLink(Object tree, File file, boolean open) {
        this.tree = tree;
        this.file = file;
        this.open = open;
    }
}
