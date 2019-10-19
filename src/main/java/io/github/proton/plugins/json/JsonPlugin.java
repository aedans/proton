package io.github.proton.plugins.json;

import io.github.proton.plugins.file.FileOpener;
import io.github.proton.plugins.file.FileType;

public final class JsonPlugin {
    public void init() {
        FileType.registry.put(new JsonFileType());
        FileOpener.registry.put(JsonFileType.class, new JsonFileOpener());
    }
}
