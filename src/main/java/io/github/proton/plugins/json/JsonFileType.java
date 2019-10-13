package io.github.proton.plugins.json;

import io.github.proton.plugins.file.FileType;

public final class JsonFileType implements FileType {
    @Override
    public String[] extensions() {
        return new String[]{"json"};
    }
}
