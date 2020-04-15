package io.github.proton.file;

import io.github.proton.plugins.Plugins;
import io.vavr.control.Option;
import org.pf4j.ExtensionPoint;

import java.io.File;

public interface FileReader<T> extends ExtensionPoint {
    @SuppressWarnings("unchecked")
    FileReader<?> instance = Plugins.getExtensions(FileReader.class).reduce(FileReader::combine);

    Option<T> read(File file);

    default FileReader<T> combine(FileReader<T> fileReader) {
        return file -> FileReader.this.read(file).orElse(() -> fileReader.read(file));
    }
}
