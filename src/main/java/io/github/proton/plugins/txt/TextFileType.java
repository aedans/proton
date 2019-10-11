package io.github.proton.plugins.txt;

import io.github.proton.plugins.file.FileLinker;
import io.github.proton.plugins.file.FileType;

public final class TextFileType implements FileType {
    static {
        FileLinker.registry.put(TextFileType.class, new TextFileLinker());
    }

    @Override
    public String[] extensions() {
        return new String[]{"txt"};
    }
}
