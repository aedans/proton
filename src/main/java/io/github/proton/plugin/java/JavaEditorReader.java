package io.github.proton.plugin.java;

import io.github.proton.api.*;
import org.pf4j.Extension;

import java.io.File;
import java.util.Optional;

@Extension
public final class JavaEditorReader implements EditorReader {
    @Override
    public Optional<Editor> read(File file) {
        if (file.getName().endsWith(".java")) {
            return Optional.of(new JavaEditor(JavaLanguageServer.instance, file));
        } else {
            return Optional.empty();
        }
    }
}
