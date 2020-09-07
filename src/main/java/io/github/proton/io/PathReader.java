package io.github.proton.io;

import io.github.proton.editor.*;
import io.github.proton.plugins.Combinable;
import io.vavr.control.Option;
import org.pf4j.*;

import java.io.IOException;
import java.nio.file.*;

public interface PathReader<T extends Tree<T>> extends ExtensionPoint, Combinable<PathReader<T>> {
    Option<T> read(Path path) throws IOException;

    @Override
    default PathReader<T> combine(PathReader<T> n) {
        return path -> read(path).orElse(() -> {
            try {
                return n.read(path);
            } catch (IOException e) {
                return Option.none();
            }
        });
    }

    @Extension
    final class Default implements PathReader<Text> {
        @Override
        public Option<Text> read(Path path) throws IOException {
            try {
                return Option.some(new Text(String.join("\n", Files.readAllLines(path))));
            } catch (IOException e) {
                return Option.none();
            }
        }

        @Override
        public int priority() {
            return Integer.MAX_VALUE;
        }
    }
}
