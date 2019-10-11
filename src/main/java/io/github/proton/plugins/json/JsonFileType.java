package io.github.proton.plugins.json;

import io.github.proton.plugins.file.FileLinker;
import io.github.proton.plugins.file.FileOpener;
import io.github.proton.plugins.file.FileType;

public final class JsonFileType implements FileType {
    static {
        FileLinker.registry.put(JsonFileType.class, new JsonFileLinker());
        FileOpener.registry.put(JsonFileType.class, new JsonFileOpener());
    }

    @Override
    public String[] extensions() {
        return new String[]{"json"};
    }
}
