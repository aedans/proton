package io.github.proton.plugins.directory;

import io.github.proton.plugins.file.FileOpener;

import java.io.File;

public final class DirectoryFileOpener implements FileOpener<Directory> {
    @Override
    public Directory open(File file) {
        return new Directory(file);
    }
}
