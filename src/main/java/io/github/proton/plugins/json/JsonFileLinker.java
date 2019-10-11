package io.github.proton.plugins.json;

import com.googlecode.lanterna.TextColor;
import io.github.proton.plugins.file.FileLink;
import io.github.proton.plugins.file.FileLinker;

import java.io.File;

public final class JsonFileLinker implements FileLinker<FileLink> {
    @Override
    public FileLink link(File file) {
        return new FileLink(file, TextColor.ANSI.GREEN, TextColor.ANSI.BLACK);
    }
}
