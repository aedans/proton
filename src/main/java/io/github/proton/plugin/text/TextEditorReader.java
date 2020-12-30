package io.github.proton.plugin.text;

import io.github.proton.api.*;
import io.vavr.control.Option;
import org.pf4j.Extension;

import java.io.File;

@Extension
public final class TextEditorReader implements EditorReader {
    @Override
    public Option<Editor> read(File file) {
        if (file.getName().endsWith(".txt")) {
            return Option.some(new TextEditor());
        } else {
            return Option.none();
        }
    }
}
