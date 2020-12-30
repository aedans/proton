package io.github.proton.plugin.text;

import io.github.proton.api.*;
import org.pf4j.Extension;

import java.io.File;
import java.util.Optional;

@Extension
public final class TextEditorReader implements EditorReader {
    @Override
    public Optional<Editor> read(File file) {
        if (file.getName().endsWith(".txt")) {
            return Optional.of(new TextEditor());
        } else {
            return Optional.empty();
        }
    }
}
