package io.github.proton.plugins.directory;

import io.github.proton.plugins.file.FileOpener;
import io.github.proton.plugins.file.FileType;

import java.io.File;

public final class DirectoryFileType implements FileType {
    static {
        FileOpener.registry.put(DirectoryFileType.class, new DirectoryFileOpener());
    }

    @Override
    public String[] extensions() {
        return new String[]{""};
    }

    @Override
    public boolean test(File file) {
        return file.isDirectory();
    }
}
