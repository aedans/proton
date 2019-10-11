package io.github.proton.plugins.file;

import io.github.proton.util.Registry;

import java.io.File;

public interface FileLinker<T> {
    Registry<FileLinker> registry = new Registry<>("file linker");
    FileLinker<Object> linker = file -> registry.getOrThrow(FileType.registry.get(file).getClass()).link(file);

    T link(File file);
}
