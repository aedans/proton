package io.github.proton.api;

import io.vavr.control.Option;
import org.pf4j.*;

import java.io.File;

public interface EditorReader extends ExtensionPoint, Plugins.Combinable<EditorReader> {
    Option<Editor> read(File file);

    @Override
    default EditorReader combine(EditorReader editorReader) {
        return file -> read(file).orElse(() -> editorReader.read(file));
    }

    EditorReader instance = Plugins.getExtension(EditorReader.class);
}
