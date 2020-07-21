package io.github.proton.io;

import io.github.proton.editor.Text;
import io.github.proton.plugins.Plugins;
import io.vavr.control.Option;
import org.pf4j.ExtensionPoint;

import java.io.IOException;
import java.nio.file.*;

public interface PathReader<T> extends ExtensionPoint {
    Option<T> read(Path path) throws IOException;

    static <T> PathReader<T> empty() {
        return path -> Option.none();
    }

    default PathReader<T> combine(PathReader<T> reader) {
        return path -> read(path).orElse(() -> {
            try {
                return reader.read(path);
            } catch (IOException e) {
                return Option.none();
            }
        });
    }

    @SuppressWarnings("unchecked")
    static Object readStatic(Path path) throws IOException {
        return Plugins.getExtensions(PathReader.class)
            .fold(PathReader.empty(), PathReader::combine)
            .read(path)
            .orElse(() -> {
                try {
                    return Option.some(new Text(String.join("\n", Files.readAllLines(path))));
                } catch (IOException e) {
                    return Option.none();
                }
            })
            .getOrNull();
    }
}
