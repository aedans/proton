package io.github.proton.plugins.json;

import com.eclipsesource.json.Json;
import io.github.proton.plugins.file.FileOpener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class JsonFileOpener implements FileOpener<JsonTree> {
    @Override
    public JsonTree open(File file) throws IOException {
        return new JsonTree(Json.parse(new FileReader(file)));
    }
}
