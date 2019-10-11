package io.github.proton.plugins.file;

import io.github.proton.util.Registry;

import java.io.File;
import java.io.IOException;

public interface FileOpener<T> {
    Registry<FileOpener> registry = new Registry<>("file opener");
    FileOpener<Object> opener = file -> registry.getOrThrow(FileType.registry.get(file).getClass()).open(file);

    T open(File file) throws IOException;
}
