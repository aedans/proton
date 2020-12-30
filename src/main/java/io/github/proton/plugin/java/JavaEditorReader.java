package io.github.proton.plugin.java;

import io.github.proton.api.*;
import io.vavr.control.Option;
import org.pf4j.Extension;

import java.io.File;

@Extension
public final class JavaEditorReader implements EditorReader {
    @Override
    public Option<Editor> read(File file) {
        if (file.getName().endsWith(".java")) {
            return Option.some(new JavaEditor(JavaLanguageServer.instance, file));
        } else {
            return Option.none();
        }
    }
}
