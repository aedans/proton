package io.github.proton.api.editor;

import io.github.proton.plugin.Plugins;
import io.github.proton.plugin.text.TextEditorReader;
import org.pf4j.ExtensionPoint;

import java.io.File;
import java.util.Optional;

public interface EditorReader extends ExtensionPoint, Plugins.Combinable<EditorReader> {
    Optional<Editor> read(File file);

    @Override
    default EditorReader combine(EditorReader editorReader) {
        return file -> read(file).or(() -> editorReader.read(file));
    }

    EditorReader instance = Plugins.getExtension(EditorReader.class, new TextEditorReader());
}
