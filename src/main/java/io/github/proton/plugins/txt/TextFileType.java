package io.github.proton.plugins.txt;

import io.github.proton.plugins.file.FileType;

public final class TextFileType implements FileType {
    @Override
    public String[] extensions() {
        return new String[]{"txt"};
    }
}
